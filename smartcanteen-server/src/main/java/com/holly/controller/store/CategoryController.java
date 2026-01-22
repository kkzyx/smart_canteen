package com.holly.controller.store;

import com.holly.dto.CategoryDTO;
import com.holly.entity.Category;
import com.holly.query.CategoryPageQueryDTO;
import com.holly.result.PageResult;
import com.holly.result.Result;
import com.holly.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description
 */
@Slf4j
@Tag(name = "分类相关接口")
@RestController
@RequestMapping("/store/category")
@RequiredArgsConstructor
public class CategoryController {
  
  private final CategoryService categoryService;
  
  /**
   * 新增分类
   * @param categoryDTO 分类数据传输对象
   */
  @PostMapping
  @Operation(summary = "新增分类")
  public Result<?> save(@RequestBody CategoryDTO categoryDTO) {
    log.info("新增分类，参数==> {}", categoryDTO);
    categoryService.save(categoryDTO);
    return Result.success();
  }
  
  /**
   * 分类分页查询
   * @param categoryPageQueryDTO 分类分页查询数据传输对象
   */
  @GetMapping("/page")
  @Operation(summary = "分类分页查询")
  public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
    log.info("分页查询，参数==> {}", categoryPageQueryDTO);
    PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
    return Result.success(pageResult);
  }
  
  /**
   * 删除分类
   * @param id 分类id
   */
  @DeleteMapping
  @Operation(summary = "删除分类")
  public Result<String> deleteById(Long id) {
    log.info("删除分类，分类id==> {}", id);
    categoryService.deleteById(id);
    return Result.success();
  }
  
  /**
   * 修改分类
   * @param categoryDTO 分类数据传输对象（由前端传过来）
   */
  @PutMapping
  @Operation(summary = "修改分类")
  public Result<String> update(@RequestBody CategoryDTO categoryDTO) {
    log.info("修改分类，参数==> {}", categoryDTO);
    categoryService.update(categoryDTO);
    return Result.success();
  }
  
  /**
   * 启用、禁用分类
   * @param status 分类状态
   * @param id 分类id
   */
  @PostMapping("/status")
  @Operation(summary = "启用禁用分类")
  public Result<String> startOrStop(Integer status, Long id) {
    log.info("启用禁用分类，分类id==> {}, 状态==> {}", id, status);
    categoryService.startOrStop(status, id);
    return Result.success();
  }
  
  /**
   * 根据类型查询分类
   * @param type 分类类型
   */
  @GetMapping("/list")
  @Operation(summary = "根据类型查询分类")
  public Result<List<Category>> list(Integer type) {
    log.info("根据类型查询分类，分类类型==> {}", type);
    List<Category> list = categoryService.list(type);
    return Result.success(list);
  }
}
