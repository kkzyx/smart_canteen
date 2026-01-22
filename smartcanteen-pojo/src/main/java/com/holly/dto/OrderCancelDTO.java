package com.holly.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description 订单取消dto
 */
@Data
@Builder
@Schema(description = "订单取消dto")
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelDTO implements Serializable {
  
  @Schema(description = "订单id")
  private Long id;
  
  @Schema(description = "订单取消原因")
  private String cancelReason;
}
