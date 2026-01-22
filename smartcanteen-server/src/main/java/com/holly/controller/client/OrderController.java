package com.holly.controller.client;

import com.holly.dto.OrderCancelDTO;
import com.holly.dto.OrdersPaymentDTO;
import com.holly.dto.OrdersSubmitDTO;
import com.holly.result.PageResult;
import com.holly.result.Result;
import com.holly.service.OrderService;
import com.holly.vo.OrderSubmitVO;
import com.holly.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @description
 */
@Slf4j
@Tag(name = "C端-订单相关接口")
@RestController("clientOrderController")
@RequestMapping("/client/order")
@RequiredArgsConstructor
public class OrderController {
  
  private final OrderService orderService;
  
  /**
   * 用户下单
   * @param ordersSubmitDTO 订单提交DTO
   */
  @PostMapping("/submit")
  @Operation(summary = "用户下单")
  public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
    log.info("用户下单，参数==> {}", ordersSubmitDTO);
    OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
    return Result.success(orderSubmitVO);
  }
  
  /**
   * 订单支付
   * @param ordersPaymentDTO 订单支付参数
   */
  @PutMapping("/payment")
  @Operation(summary = "订单支付")
  public Result<?> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) {
    /* 模拟支付成功 */
    orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
    log.info("模拟交易成功==> {}", ordersPaymentDTO.getOrderNumber());
    return Result.success();
  }
  
  /**
   * 客户催单
   */
  @GetMapping("/reminder")
  @Operation(summary = "客户催单")
  public Result<?> reminder(@RequestParam Long id) {
    orderService.reminder(id);
    return Result.success();
  }
  
  /**
   * 历史订单查询
   * @param page 页码
   * @param pageSize 每页数量
   * @param status 订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
   */
  @GetMapping("/historyOrders")
  @Operation(summary = "历史订单查询")
  public Result<PageResult> page(int page, int pageSize, Integer status) {
    PageResult pageResult = orderService.pageQueryUser(page, pageSize, status);
    return Result.success(pageResult);
  }
  
  /**
   * 根据id查询订单详情
   * @param id 订单id
   */
  @GetMapping("/orderDetail")
  @Operation(summary = "根据id查询订单详情")
  public Result<OrderVO> details(@RequestParam Long id) {
    OrderVO orderVO = orderService.details(id);
    return Result.success(orderVO);
  }
  
  /**
   * 用户取消订单
   * @param orderCancelDTO 订单取消dto
   */
  @PutMapping("/cancel")
  @Operation(summary = "用户取消订单")
  public Result<?> cancel(@RequestBody OrderCancelDTO orderCancelDTO) throws Exception {
    orderService.userCancelById(orderCancelDTO);
    return Result.success();
  }
  
  /**
   * 再来一单
   * @param id 订单id
   */
  @PutMapping("/repetition/{id}")
  @Operation(summary = "再来一单")
  public Result<?> repetition(@PathVariable Long id) {
    orderService.repetition(id);
    return Result.success();
  }
}
