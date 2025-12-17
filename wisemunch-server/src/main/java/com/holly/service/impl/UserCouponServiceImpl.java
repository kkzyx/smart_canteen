package com.holly.service.impl;

import com.holly.context.BaseContext;
import com.holly.entity.Coupon;
import com.holly.entity.UserCoupon;
import com.holly.exception.BaseException;
import com.holly.mapper.CouponMapper;
import com.holly.mapper.UserCouponMapper;
import com.holly.service.UserCouponService;
import com.holly.vo.UserCouponVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户优惠券服务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {
    
    private final UserCouponMapper userCouponMapper;
    private final CouponMapper couponMapper;
    
    @Override
    @Transactional
    public void receive(Long couponId) {
        Long userId = BaseContext.getUserId();
        
        // 查询优惠券信息
        Coupon coupon = couponMapper.getById(couponId);
        if (coupon == null) {
            throw new BaseException("优惠券不存在");
        }
        
        // 检查优惠券状态
        if (coupon.getStatus().equals(Coupon.STATUS_DISABLED)) {
            throw new BaseException("优惠券已禁用");
        }
        
        // 检查是否过期
        if (coupon.getEndTime().isBefore(LocalDateTime.now())) {
            throw new BaseException("优惠券已过期");
        }
        
        // 检查剩余数量
        if (coupon.getRemainCount() <= 0) {
            throw new BaseException("优惠券已被领完");
        }
        
        // 检查用户领取数量
        int receivedCount = userCouponMapper.countByUserIdAndCouponId(userId, couponId);
        if (receivedCount >= coupon.getPerLimit()) {
            throw new BaseException("您已达到该优惠券的领取上限");
        }
        
        // 减少优惠券剩余数量
        int updated = couponMapper.decreaseRemainCount(couponId);
        if (updated == 0) {
            throw new BaseException("优惠券库存不足");
        }
        
        // 保存用户优惠券
        UserCoupon userCoupon = UserCoupon.builder()
                .userId(userId)
                .couponId(couponId)
                .status(UserCoupon.STATUS_UNUSED)
                .receiveTime(LocalDateTime.now())
                .build();
        userCouponMapper.insert(userCoupon);
    }
    
    @Override
    public List<UserCouponVO> listMyCoupons(Integer status) {
        Long userId = BaseContext.getUserId();
        // 先更新过期优惠券
        userCouponMapper.updateExpiredCoupons();
        return userCouponMapper.listByUserId(userId, status);
    }
    
    @Override
    public List<UserCouponVO> listAvailableForOrder(BigDecimal orderAmount) {
        Long userId = BaseContext.getUserId();
        // 更新过期优惠券
        userCouponMapper.updateExpiredCoupons();
        
        // 查询未使用的优惠券
        List<UserCouponVO> coupons = userCouponMapper.listByUserId(userId, UserCoupon.STATUS_UNUSED);
        
        // 过滤并计算可用性
        return coupons.stream()
                .filter(coupon -> {
                    // 检查是否满足使用门槛
                    return orderAmount.compareTo(coupon.getMinAmount()) >= 0;
                })
                .peek(coupon -> {
                    coupon.setCanUse(true);
                })
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void use(Long userCouponId, Long orderId) {
        log.info("=== 开始使用优惠券 ===");
        log.info("userCouponId: {}, orderId: {}", userCouponId, orderId);
        
        UserCoupon userCoupon = userCouponMapper.getById(userCouponId);
        if (userCoupon == null) {
            log.error("优惠券不存在，userCouponId: {}", userCouponId);
            throw new BaseException("优惠券不存在");
        }
        
        log.info("查询到优惠券 - id: {}, status: {}, userId: {}, couponId: {}", 
                 userCoupon.getId(), userCoupon.getStatus(), userCoupon.getUserId(), userCoupon.getCouponId());
        
        if (!userCoupon.getStatus().equals(UserCoupon.STATUS_UNUSED)) {
            log.error("优惠券状态不是未使用，当前status: {}", userCoupon.getStatus());
            throw new BaseException("优惠券不可用");
        }
        
        userCoupon.setStatus(UserCoupon.STATUS_USED);
        userCoupon.setOrderId(orderId);
        userCoupon.setUseTime(LocalDateTime.now());
        
        log.info("准备更新优惠券状态 - 设置status=1, orderId: {}, useTime: {}", orderId, userCoupon.getUseTime());
        
        int rows = userCouponMapper.updateStatus(userCoupon);
        log.info("更新完成，影响行数: {}", rows);
        
        if (rows == 0) {
            log.error("更新优惠券状态失败，影响行数为0");
            throw new BaseException("更新优惠券状态失败");
        }
        
        log.info("=== 优惠券使用成功 ===");
    }
    
    @Override
    @Transactional
    public void refund(Long orderId) {
        log.info("退还订单{}的优惠券", orderId);
        // 实际项目中需要实现退还逻辑
    }
}
