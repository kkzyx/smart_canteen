package com.holly.dto;

import com.holly.entity.DishFlavor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @description
 */
@Data
@Schema(description = "菜品传输对象")
public class DishDTO implements Serializable {
  
  @Schema(description = "菜品id")
  private Long id;
  
  @Schema(description = "菜品名称")
  private String name;
  
  @Schema(description ="菜品分类id")
  private Long categoryId;
  
  @Schema(description ="菜品价格")
  private BigDecimal price;
  
  @Schema(description ="菜品图片url")
  private String image;
  
  @Schema(description ="菜品描述信息")
  private String description;
  
  @Schema(description ="菜品状态，0-停售 1-起售")
  private Integer status;
  
  @Schema(description ="菜品口味，可以有多个口味")
  private List<DishFlavor> flavors = new ArrayList<>();
}
