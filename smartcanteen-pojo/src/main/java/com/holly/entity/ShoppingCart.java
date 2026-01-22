package com.holly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @description
 */
@Data
@Builder
@Schema(description = "购物车实体")
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  @Schema(description = "购物车id")
  private Long id;
  
  @Schema(description = "商品名称")
  private String name;
  
  @Schema(description = "用户id")
  private Long userId;
  
  @Schema(description = "菜品id")
  private Long dishId;
  
  @Schema(description = "套餐id")
  private Long setmealId;
  
  @Schema(description = "菜品口味")
  private String dishFlavor;
  
  @Schema(description = "数量")
  private Integer number;
  
  @Schema(description = "金额")
  private BigDecimal amount;
  
  @Schema(description = "图片")
  private String image;
  
  @Schema(description = "创建时间")
  private LocalDateTime createTime;
}
