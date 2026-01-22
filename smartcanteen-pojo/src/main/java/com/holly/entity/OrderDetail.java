package com.holly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @description
 */
@Data
@Builder
@Schema(description = "订单明细")
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
  private static final long serialVersionUID = 1L;
  
  @Schema(description = "订单明细id")
  private Long id;
  
  @Schema(description = "商品名称")
  private String name;
  
  @Schema(description = "订单id")
  private Long orderId;
  
  @Schema(description = "菜品id")
  private Long dishId;

  @Schema(description = "套餐id")
  private Long setmealId;
  
  @Schema(description = "数量")
  private Integer number;
  
  @Schema(description = "金额")
  private BigDecimal amount;
  
  @Schema(description = "图片")
  private String image;
}
