package com.holly.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @description
 */
@Data
@Schema(description = "分类数据传输对象")
public class CategoryDTO implements Serializable {
  @Schema(description = "分类id")
  private Long id;
  
  @Schema(description = "类型，1-菜品分类 2-套餐分类")
  private Integer type;
  
  @Schema(description = "分类名称")
  private String name;
  
  @Schema(description = "排序")
  private Integer sort;
}
