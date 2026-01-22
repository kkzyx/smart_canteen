package com.holly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @description 菜品口味
 */
@Data
@Builder
@Schema(description = "菜品口味")
@NoArgsConstructor
@AllArgsConstructor
public class DishFlavor implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  @Schema(description = "菜品口味id")
  private Long id;
  
  @Schema(description = "菜品id")
  private Long dishId;
  
  @Schema(description = "口味名称")
  private String name;
  
  @Schema(description = "口味值")
  private String value;
  
}
