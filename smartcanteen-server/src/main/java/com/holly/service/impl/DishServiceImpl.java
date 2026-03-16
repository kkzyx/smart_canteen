package com.holly.service.impl;

import com.holly.constant.CachePrefixConstant;
import com.holly.constant.MessageConstant;
import com.holly.constant.StatusConstant;
import com.holly.dto.DishDTO;
import com.holly.entity.Dish;
import com.holly.entity.DishFlavor;
import com.holly.exception.DeletionNotAllowedException;
import com.holly.exception.DishExecption;
import com.holly.mapper.*;
import com.holly.query.DishPageQueryDTO;
import com.holly.result.PageResult;
import com.holly.service.DishService;
import com.holly.service.ViewService;
import com.holly.vo.DishVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.holly.constant.MessageConstant.INVALID_PARAM;

/**
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishMapper dishMapper;
    private final SetmealMapper setmealMapper;
    private final CategoryMapper categoryMapper;
    private final DishFlavorMapper dishFlavorMapper;
    private final SetmealDishMapper setmealDishMapper;
    private final FileServiceImpl fileService;
    private final ViewService viewService;
    private final RedisTemplate<String,Object> redisTemplate;
    @Override
    public List<DishVO> listWithCache(Long categoryId, Integer tabIndex) {
        // 1. 构建目录化的 Key 结构：dish:list:categoryId_tabIndex
        // 这样在 Redis 客户端中会显示为 dish -> list -> 具体文件
        String key = CachePrefixConstant.DISH_KEY+ (categoryId == null ? "all" : categoryId) + "_tab" + tabIndex;

        // 2. 查询缓存
        List<DishVO> cacheList = (List<DishVO>)redisTemplate.opsForValue().get(key);
        if (cacheList != null && !cacheList.isEmpty()) {
            log.info("查询到 Redis 缓存，直接返回: {}", key);
            return cacheList;
        }

        // 3. 缓存不存在，查询数据库
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);

        // 调用原有的 listWithFlavors 方法获取基础数据
        List<DishVO> list = listWithFlavors(dish);

        // 4. 根据 tabIndex 处理逻辑
        if (tabIndex != 0) {
            // 热度菜品：根据热度排序（从高到低）
            list.sort(Comparator.comparing(DishVO::getHotSpot,
                    Comparator.nullsLast(Comparator.reverseOrder())));
        }

        // 5. 存入 Redis 并设置过期时间（建议设置过期时间防止脏数据长期驻留）
        redisTemplate.opsForValue().set(key, list, 60, TimeUnit.MINUTES);

        return list;
    }

    @Transactional
    @Override
    public void saveWithFlavors(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.insert(dish);
        // 获取插入之后菜品的id值
        Long dishId = dish.getId();

        // 处理多种口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            // 将口味与对应菜品进行关联
            flavors.forEach(flavor -> flavor.setDishId(dishId));
            dishFlavorMapper.insertBatch(flavors);
        }
        this.cleanAllDishCache();
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        if (dishPageQueryDTO == null){
            throw new DishExecption(INVALID_PARAM);
        }
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> pageInfo = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(pageInfo.getTotal(), pageInfo.getResult());
    }

    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        // 判断当前菜品是否处于起售状态，如果处于起售状态，则不能删除
        List<Dish> dishList = dishMapper.getDishByIds(ids);
        for (Dish dish : dishList) {
            if (StatusConstant.ENABLE.equals(dish.getStatus())) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 判断当前菜品是否被套餐关联，如果被套餐关联，则不能删除
        List<Long> setmealIds = setmealDishMapper.getSetmealDishByDishIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 删除了菜品，对应的口味数据也应该被删除
        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);
        //删除minio中的文件
        dishList.forEach(dish -> {
            String image = dish.getImage();
            if (image != null) {
                log.info("删除菜品图片：{}", image);
                //minio版
                fileService.deleteFileMinio(image);
                //阿里云版
//                fileService.deleteFileAliOss(image);
            }
        });
        this.cleanAllDishCache();
    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        // 根据菜品id查询菜品数据
        Dish dish = dishMapper.getDishById(id);

        // 查询该菜品所属分类名称
        String categoryName = categoryMapper.getCategoryNameById(dish.getCategoryId());

        // 根据菜品id查询口味数据
        List<DishFlavor> flavors = dishFlavorMapper.getFlavorsByDishId(id);

        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        dishVO.setCategoryName(categoryName);
        return dishVO;
    }

    @Transactional
    @Override
    public void updateWithFlavors(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 先更新菜品表中的数据
        dishMapper.update(dish);
        // 先删除原有的口味数据
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        // 再插入新的口味数据（如果是移除了口味，则不需要插入新的口味数据）
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(flavor -> flavor.setDishId(dishDTO.getId()));
            dishFlavorMapper.insertBatch(flavors);
        }
        this.cleanAllDishCache();
    }

    @Transactional
    @Override
    public void startOrStop(Integer status, Long id) {
        // 1、先更新菜品表，将菜品的状态更新为`status`
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);

        // 停售一个菜品，那么包含这个菜品的套餐也应该停售
        if (!StatusConstant.DISABLE.equals(status)) return;
        List<Long> dishIds = new ArrayList<>();
        dishIds.add(id);

        // 查询包含该菜品的套餐（返回套餐id），原型：select `setmeal_id` from `setmeal_dish` where `dish_id` in (?,?,?)
        List<Long> setmealIds = setmealDishMapper.getSetmealDishByDishIds(dishIds);
        if (setmealIds.size() == 0) return;
        setmealMapper.batchUpdateStatus(setmealIds, StatusConstant.DISABLE);
        // 清理缓存
        this.cleanAllDishCache();
    }

    @Override
    public List<DishVO> listWithFlavors(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();
        Long categoryId = null;
        //查询分类名称
        String categoryName = null;
        if (dishList != null && !dishList.isEmpty()) {
            categoryId = dishList.get(0).getCategoryId();
            categoryName = categoryMapper.getCategoryNameById(categoryId);
        }


        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);
            if (categoryId != null) {
                dishVO.setCategoryName(categoryName);
            }

            // 根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getFlavorsByDishId(d.getId());
            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    @Override
    public List<DishVO> listRecommendWithCache() {
        String key = CachePrefixConstant.RECOMMEND_DISH_KEY;
        //从redis中查询是否有推荐菜品
        List<DishVO> dishVOS = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if (dishVOS != null && !dishVOS.isEmpty()) {
          return dishVOS;
        }
        //没有缓存查询数据库
        Dish dish = new Dish();
        List<DishVO> list = listWithFlavors(dish);
        List<DishVO> fourDish = list.subList(0, Math.min(list.size(), 4));
        //存入缓存
        redisTemplate.opsForValue().set(key, fourDish);
        return fourDish;
    }
    // 抽取一个统一的清理方法，避免到处写字符串拼接
    private void cleanAllDishCache() {
        clearCache("dish:*");     // 清理所有菜品相关（列表、推荐等）
        clearCache("setmealCache:*");  // 清理套餐（因为菜品变了，套餐数据可能也过期了）
    }
    private void clearCache(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && keys.size() > 0) {
            redisTemplate.delete(keys);
            log.info("清理缓存成功，pattern：{}", pattern);
        }
    }
}
