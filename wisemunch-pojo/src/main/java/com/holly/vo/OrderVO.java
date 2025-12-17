package com.holly.vo;

import com.holly.entity.OrderDetail;
import com.holly.entity.Orders;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description
 */
@Data
@ApiModel(description = "订单视图对象")
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO  extends Orders implements Serializable {
  
  @ApiModelProperty("订单菜品信息")
  private String orderDishes;
  
  @ApiModelProperty("订单详情")
  private List<OrderDetail> orderDetailList;

  @ApiModelProperty("订单类型名称")
  private String orderTypeName;
}
