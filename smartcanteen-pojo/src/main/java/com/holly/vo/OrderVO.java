package com.holly.vo;

import com.holly.entity.OrderDetail;
import com.holly.entity.Orders;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description
 */
@Data
@Schema(description = "订单视图对象")
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO  extends Orders implements Serializable {
  
  @Schema(description = "订单菜品信息")
  private String orderDishes;
  
  @Schema(description = "订单详情")
  private List<OrderDetail> orderDetailList;

  @Schema(description = "订单类型名称")
  private String orderTypeName;
}
