package com.holly.service;

import com.holly.dto.OrderCancelDTO;
import com.holly.dto.OrdersCancelDTO;
import com.holly.dto.OrdersRejectionDTO;
import com.holly.dto.OrdersSubmitDTO;
import com.holly.query.OrdersPageQueryDTO;
import com.holly.result.PageResult;
import com.holly.vo.OrderSubmitVO;
import com.holly.vo.OrderVO;

/**
 * @description
 */
public interface OrderService {
  /**
   * 订单搜索条件查询
   * @param ordersPageQueryDTO 订单搜索条件
   * @return 分页结果
   */
  PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);
  
  /**
   * 根据订单id查询订单详情
   * @param id 订单id
   * @return 订单详情
   */
  OrderVO details(Long id);
  
  /**
   * 商家接单
   * @param id 订单id
   */
  void confirm(Long id);
  
  /**
   * 商家拒单
   * @param ordersRejectionDTO 订单拒单DTO
   */
  void rejection(OrdersRejectionDTO ordersRejectionDTO);
  
  /**
   * 商家取消订单
   * @param ordersCancelDTO 订单取消DTO
   */
  void cancel(OrdersCancelDTO ordersCancelDTO);
  
  /**
   * 派送订单
   * @param id 订单id
   */
  void delivery(Long id);
  
  /**
   * 订单完成
   * @param id 订单id
   */
  void complete(Long id);
  
  /**
   * 用户下单
   * @param ordersSubmitDTO 订单信息
   * @return 订单提交返回结果
   */
  OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
  
  /**
   * 客户催单
   * @param id 订单id
   */
  void reminder(Long id);
  
  /**
   * 用户端订单分页查询
   * @param page 页码
   * @param pageSize 每页数量
   * @param status 订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
   * @return 分页结果
   */
  PageResult pageQueryUser(int page, int pageSize, Integer status);
  
  /**
   * 用户根据订单id取消订单
   * @param orderCancelDTO 订单取消dto
   */
  void userCancelById(OrderCancelDTO orderCancelDTO) throws Exception;
  
  /**
   * 用户再来一单
   * @param id 订单id
   */
  void repetition(Long id);
  
  void paySuccess(String orderNumber);
}
