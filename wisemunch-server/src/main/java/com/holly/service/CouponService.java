package com.holly.service;

import com.holly.dto.CouponDTO;
import com.holly.dto.CouponPageQueryDTO;
import com.holly.result.PageResult;
import com.holly.vo.CouponVO;

import java.util.List;

/**
 * 优惠券服务接口
 */
public interface CouponService {
    
    /**
     * 新增优惠券
     */
    void save(CouponDTO couponDTO);
    
    /**
     * 修改优惠券
     */
    void update(CouponDTO couponDTO);
    
    /**
     * 删除优惠券
     */
    void deleteById(Long id);
    
    /**
     * 根据id查询优惠券
     */
    CouponVO getById(Long id);
    
    /**
     * 分页查询优惠券
     */
    PageResult pageQuery(CouponPageQueryDTO couponPageQueryDTO);
    
    /**
     * 启用/禁用优惠券
     */
    void startOrStop(Integer status, Long id);
    
    /**
     * 查询所有可领取的优惠券（用户端）
     */
    List<CouponVO> listAvailable(Long userId);
}
