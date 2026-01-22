package com.holly.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @description
 */
@Data
@Builder
@Schema(description = "套餐中的菜品项")
@NoArgsConstructor
@AllArgsConstructor
public class DishItemVO implements Serializable {
  
  @Schema(description = "菜品名称")
  private String name;
  
  @Schema(description = "份数")
  private Integer copies;
  
  @Schema(description = "菜品图片")
  private String image;
  
  @Schema(description = "菜品描述")
  private String description;
}
