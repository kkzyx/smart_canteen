package com.holly.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @description
 */
@Data
@Schema(description = "购物车DTO")
public class ShoppingCartDTO implements Serializable {

  @Schema(description = "菜品id")
  private Long dishId;

  @Schema(description = "套餐id")
  private Long setmealId;

  @Schema(description = "数量")
  private int number;

  @Schema(description = "菜品口味，多个不同口味使用逗号分隔")
  private String dishFlavor;
}

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//import java.io.Serializable;
//
///**
// * @description
// */
//@Data
//@ApiModel(description = "购物车DTO")
//public class ShoppingCartDTO implements Serializable {
//
//  @ApiModelProperty("菜品id")
//  private Long dishId;
//
//  @ApiModelProperty("套餐id")
//  private Long setmealId;
//
//  @ApiModelProperty(value = "数量", notes = "可选参数，默认为null")
//  private int number;
//
//  @ApiModelProperty("菜品口味，多个不同口味使用逗号分隔")
//  private String dishFlavor;
//}