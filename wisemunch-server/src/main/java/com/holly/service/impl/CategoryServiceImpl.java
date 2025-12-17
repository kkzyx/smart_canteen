package com.holly.service.impl;

import com.holly.constant.MessageConstant;
import com.holly.constant.StatusConstant;
import com.holly.dto.CategoryDTO;
import com.holly.entity.Category;
import com.holly.exception.DeletionNotAllowedException;
import com.holly.mapper.CategoryMapper;
import com.holly.mapper.DishMapper;
import com.holly.query.CategoryPageQueryDTO;
import com.holly.result.PageResult;
import com.holly.service.CategoryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  
  private final CategoryMapper categoryMapper;
  private final DishMapper dishMapper;
  
  @Override
  public void save(CategoryDTO categoryDTO) {
    Category category = new Category();
    BeanUtils.copyProperties(categoryDTO, category);
    category.setStatus(StatusConstant.DISABLE);
    
    categoryMapper.insert(category);
  }
  
  @Override
  public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
    int page = categoryPageQueryDTO.getPage();
    int pageSize = categoryPageQueryDTO.getPageSize();
    
    PageHelper.startPage(page, pageSize);
    Page<Category> categories = categoryMapper.pageQuery(categoryPageQueryDTO);
    
    return new PageResult(categories.getTotal(), categories.getResult());
  }
  
  @Override
  public void deleteById(Long id) {
    // 查询当前分类是否关联了菜品，如果关联了就抛出业务异常
    Integer count = dishMapper.countByCategoryId(id);
    if (count > 0) {
      // 当前分类下有菜品，不能删除
      throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
    }
    
    // 删除分类数据
    categoryMapper.deleteById(id);
  }
  
  @Override
  public void update(CategoryDTO categoryDTO) {
    Category category = new Category();
    BeanUtils.copyProperties(categoryDTO, category);
    categoryMapper.update(category);
  }
  
  @Override
  public void startOrStop(Integer status, Long id) {
    Category category = Category.builder()
            .id(id)
            .status(status)
            .build();
    categoryMapper.update(category);
  }
  
  @Override
  public List<Category> list(Integer type) {
    return categoryMapper.list(type);
  }
}
