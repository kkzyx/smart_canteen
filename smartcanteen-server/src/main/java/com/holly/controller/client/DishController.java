package com.holly.controller.client;

import com.holly.constant.CachePrefixConstant;
import com.holly.constant.StatusConstant;
import com.holly.entity.Dish;
import com.holly.result.Result;
import com.holly.service.DishService;
import com.holly.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @description
 */
@Slf4j
@Tag(name = "C端-菜品浏览接口")
@RestController("clientDishController")
@RequestMapping("/client/dish")
@RequiredArgsConstructor
public class DishController {
  
  private final DishService dishService;
  private final RedisTemplate<String, Object> redisTemplate;
  
  /**
   * 根据分类id查询菜品
   * @param categoryId 分类id
   * @param tabIndex 顶部标签 0 全部菜品 1热度菜品
   * @return 菜品列表
   */
  @GetMapping("/list")
  @Operation(summary = "根据分类id查询菜品")
  public Result<List<DishVO>> list(@RequestParam(value = "categoryId",required = false) Long categoryId, @RequestParam("tabIndex") Integer tabIndex) {
  // 直接调用 Service 层的缓存增强方法
    List<DishVO> list = dishService.listWithCache(categoryId, tabIndex);
    return Result.success(list);
  }

  @GetMapping("/{id}")
  @Operation(summary = "根据id查询菜品详情")
  public Result<DishVO> getById(@PathVariable Long id) {
    DishVO dishVO = dishService.getByIdWithFlavor(id);
    return Result.success(dishVO);
  }

  @GetMapping("/recommend")
  @Operation(summary = "获取前四项作为推荐菜品")
  public Result<List<DishVO>> getDish() {
    List<DishVO> dishVOS = dishService.listRecommendWithCache();
    return Result.success(dishVOS);
  }
}
