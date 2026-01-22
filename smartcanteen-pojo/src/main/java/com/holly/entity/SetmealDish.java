package com.holly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @description 套餐菜品关系
 */
@Data
@Builder
@Schema(description = "套餐菜品关系")
@NoArgsConstructor
@AllArgsConstructor
public class SetmealDish implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  @Schema(description = "套餐菜品id")
  private Long id;
  
  @Schema(description = "套餐id")
  private Long setmealId;
  
  @Schema(description = "菜品id")
  private Long dishId;
  
  @Schema(description = "菜品名称")
  private String name;
  
  @Schema(description = "菜品原价")
  private BigDecimal price;
  
  @Schema(description = "份数")
  private Integer copies;
  
  @Schema(description = "套餐中菜品图片")
  private String image;
}
