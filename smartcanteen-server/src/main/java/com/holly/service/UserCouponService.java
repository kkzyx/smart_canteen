package com.holly.service;

import com.holly.vo.UserCouponVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户优惠券服务接口
 */
public interface UserCouponService {
    
    /**
     * 用户领取优惠券
     */
    void receive(Long couponId);
    
    /**
     * 查询用户的优惠券列表
     */
    List<UserCouponVO> listMyCoupons(Integer status);
    
    /**
     * 查询用户可用于某订单的优惠券
     */
    List<UserCouponVO> listAvailableForOrder(BigDecimal orderAmount);
    
    /**
     * 使用优惠券
     */
    void use(Long userCouponId, Long orderId);
    
    /**
     * 退还优惠券（订单取消时）
     */
    void refund(Long orderId);
}
