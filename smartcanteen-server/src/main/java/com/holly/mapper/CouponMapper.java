package com.holly.mapper;

import com.github.pagehelper.Page;
import com.holly.dto.CouponPageQueryDTO;
import com.holly.entity.Coupon;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 优惠券Mapper
 */
@Mapper
public interface CouponMapper {
    
    /**
     * 插入优惠券
     */
    @Insert("INSERT INTO coupon (name, type, discount_amount, discount_rate, min_amount, " +
            "total_count, remain_count, per_limit, start_time, end_time, status, description, " +
            "create_time, update_time, create_user, update_user) " +
            "VALUES (#{name}, #{type}, #{discountAmount}, #{discountRate}, #{minAmount}, " +
            "#{totalCount}, #{remainCount}, #{perLimit}, #{startTime}, #{endTime}, #{status}, " +
            "#{description}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Coupon coupon);
    
    /**
     * 更新优惠券
     */
    void update(Coupon coupon);
    
    /**
     * 根据id删除优惠券
     */
    @Delete("DELETE FROM coupon WHERE id = #{id}")
    void deleteById(Long id);
    
    /**
     * 根据id查询优惠券
     */
    @Select("SELECT * FROM coupon WHERE id = #{id}")
    Coupon getById(Long id);
    
    /**
     * 分页查询优惠券
     */
    Page<Coupon> pageQuery(CouponPageQueryDTO couponPageQueryDTO);
    
    /**
     * 查询所有启用的优惠券
     */
    @Select("SELECT * FROM coupon WHERE status = 1 AND end_time > NOW() ORDER BY create_time DESC")
    List<Coupon> listEnabled();
    
    /**
     * 减少优惠券剩余数量
     */
    @Update("UPDATE coupon SET remain_count = remain_count - 1 WHERE id = #{id} AND remain_count > 0")
    int decreaseRemainCount(Long id);
    
    /**
     * 增加优惠券剩余数量（用户退还优惠券）
     */
    @Update("UPDATE coupon SET remain_count = remain_count + 1 WHERE id = #{id}")
    void increaseRemainCount(Long id);
}
