package com.holly.controller.client;

import com.holly.entity.Category;
import com.holly.result.Result;
import com.holly.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description
 */
@Slf4j
@Tag(name = "C端-分类接口")
@RestController("clientCategoryController")
@RequestMapping("/client/category")
@RequiredArgsConstructor
public class CategoryController {
  
  private final CategoryService categoryService;
  
  /**
   * 查询分类
   * @param type 分类类型，1-菜品
   * @return 分类列表
   */
  @GetMapping("/list")
  @Operation(summary = "查询分类")
  public Result<List<Category>> list(Integer type) {
    List<Category> categoryList = categoryService.list(type);
    return Result.success(categoryList);
  }
}
