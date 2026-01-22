package com.holly.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @description 订单概览数据
 */
@Data
@Builder
@Schema(description = "查询订单管理数据")
@NoArgsConstructor
@AllArgsConstructor
public class OrderOverViewVO implements Serializable {
  
  /** 待接单数量 */
  @Schema(description = "待接单数量")
  private long waitingOrders;
  
  /** 待派送数量 */
  @Schema(description = "待派送数量")
  private long deliveredOrders;
  
  /** 已完成数量 */
  @Schema(description = "已完成数量")
  private long completedOrders;
  
  /** 已取消数量 */
  @Schema(description = "已取消数量")
  private long cancelledOrders;
  
  /** 全部订单 */
  @Schema(description = "全部订单")
  private long allOrders;
}
