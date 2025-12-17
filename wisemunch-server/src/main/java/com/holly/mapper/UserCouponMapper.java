package com.holly.mapper;

import com.holly.entity.UserCoupon;
import com.holly.vo.UserCouponVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户优惠券Mapper
 */
@Mapper
public interface UserCouponMapper {
    
    /**
     * 插入用户优惠券
     */
    @Insert("INSERT INTO user_coupon (user_id, coupon_id, status, receive_time) " +
            "VALUES (#{userId}, #{couponId}, #{status}, #{receiveTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(UserCoupon userCoupon);
    
    /**
     * 查询用户领取某个优惠券的数量
     */
    @Select("SELECT COUNT(*) FROM user_coupon WHERE user_id = #{userId} AND coupon_id = #{couponId}")
    int countByUserIdAndCouponId(@Param("userId") Long userId, @Param("couponId") Long couponId);
    
    /**
     * 查询用户的优惠券列表（含优惠券详情）
     */
    List<UserCouponVO> listByUserId(@Param("userId") Long userId, @Param("status") Integer status);
    
    /**
     * 根据id查询用户优惠券
     */
    @Select("SELECT * FROM user_coupon WHERE id = #{id}")
    UserCoupon getById(Long id);
    
    /**
     * 更新用户优惠券状态
     * @return 更新的行数
     */
    @Update("UPDATE user_coupon SET status = #{status}, order_id = #{orderId}, use_time = #{useTime} " +
            "WHERE id = #{id}")
    int updateStatus(UserCoupon userCoupon);
    
    /**
     * 批量更新过期优惠券状态
     */
    @Update("UPDATE user_coupon uc " +
            "INNER JOIN coupon c ON uc.coupon_id = c.id " +
            "SET uc.status = 2 " +
            "WHERE uc.status = 0 AND c.end_time < NOW()")
    void updateExpiredCoupons();
}
