package com.holly.controller.store;

import com.holly.constant.CachePrefixConstant;
import com.holly.dto.DishDTO;
import com.holly.query.DishPageQueryDTO;
import com.holly.result.PageResult;
import com.holly.result.Result;
import com.holly.service.DishService;
import com.holly.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @description
 */
@Slf4j
@Api(tags = "菜品管理")
@RestController
@RequestMapping("/store/dish")
@RequiredArgsConstructor
public class DishController {
  
  private final DishService dishService;
  private final RedisTemplate<String, ?> redisTemplate;
  
  /**
   * 新增菜品
   * @param dishDTO 新增菜品数据传输对象（由前端传过来）
   */
  @PostMapping
  @ApiOperation("新增菜品")
  public Result<?> save(@RequestBody DishDTO dishDTO) {
    log.info("新增菜品信息==> {}", dishDTO);
    dishService.saveWithFlavors(dishDTO);
    String tabIndexKey1 = CachePrefixConstant.DISH_KEY + dishDTO.getCategoryId() + "_" + 0;//0表示未起售
    String tabIndexKey2 = CachePrefixConstant.DISH_KEY + dishDTO.getCategoryId() + "_" + 1;//1表示起售
    String recommentKey = CachePrefixConstant.RECOMMEND_DISH_KEY;//推荐菜品
    // 新增菜品时，清除缓存菜品数据
    clearCache(tabIndexKey1);
    clearCache(tabIndexKey2);
    clearCache(recommentKey);
    
    return Result.success();
  }
  
  /**
   * 分页查询菜品数据
   * @param dishPageQueryDTO 菜品分页查询数据传输对象
   */
  @GetMapping("/page")
  @ApiOperation("分页查询菜品数据")
  public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
    log.info("分页查询菜品信息==> {}", dishPageQueryDTO);
    PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
    return Result.success(pageResult);
  }
  
  /**
   * 批量删除菜品
   * @param ids 菜品id集合，以逗号分隔，如：1,2,3
   */
  @DeleteMapping
  @ApiOperation("批量删除菜品")
  public Result<?> delete(@RequestParam List<Long> ids) {
    log.info("批量删除菜品，ids==> {}", ids);
    dishService.deleteBatch(ids);
    // 删除所有以`dish_`为前缀的菜品缓存数据
    clearCache(CachePrefixConstant.DISH_KEY + "*");
    clearCache(CachePrefixConstant.RECOMMEND_DISH_KEY);
    return Result.success();
  }
  
  /**
   * 根据菜品id查询菜品详情
   * @param id 菜品id
   */
  @GetMapping
  @ApiOperation("根据菜品id查询菜品详情")
  public Result<DishVO> getDishById(@RequestParam Long id) {
    log.info("根据菜品id查询菜品详情，id==> {}", id);
    DishVO dishVO = dishService.getByIdWithFlavor(id);
    return Result.success(dishVO);
  }
  
  /**
   * 更新菜品信息
   * @param dishDTO 菜品数据传输对象
   */
  @PutMapping
  @ApiOperation("更新菜品信息")
  public Result<?> update(@RequestBody DishDTO dishDTO) {
    log.info("更新菜品信息==> {}", dishDTO);
    dishService.updateWithFlavors(dishDTO);
    clearCache(CachePrefixConstant.DISH_KEY + "*");
    clearCache(CachePrefixConstant.RECOMMEND_DISH_KEY);
    return Result.success();
  }
  
  /**
   * 菜品起售停售
   * @param id 菜品id
   * @param status 菜品状态，1表示起售，0表示停售
   */
  @PostMapping("/status/{status}")
  @ApiOperation("菜品起售停售")
  public Result<String> startOrStop(@PathVariable("status") Integer status, Long id) {
    dishService.startOrStop(status, id);
    clearCache(CachePrefixConstant.DISH_KEY + "*");
    return Result.success();
  }
  
  /**
   * 清理redis中的缓存数据
   * @param pattern 缓存key
   */
  private void clearCache(String pattern) {
    Set<String> keys = redisTemplate.keys(pattern);
    if (keys != null && keys.size() > 0) {
      redisTemplate.delete(keys);
    }
  }
}
