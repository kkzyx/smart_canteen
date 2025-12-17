package com.holly.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.holly.context.BaseContext;
import com.holly.dto.CouponDTO;
import com.holly.dto.CouponPageQueryDTO;
import com.holly.entity.Coupon;
import com.holly.mapper.CouponMapper;
import com.holly.mapper.UserCouponMapper;
import com.holly.result.PageResult;
import com.holly.service.CouponService;
import com.holly.vo.CouponVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券服务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    
    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;
    
    @Override
    public void save(CouponDTO couponDTO) {
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(couponDTO, coupon);
        
        coupon.setRemainCount(couponDTO.getTotalCount());
        coupon.setCreateTime(LocalDateTime.now());
        coupon.setUpdateTime(LocalDateTime.now());
        coupon.setCreateUser(BaseContext.getUserId());
        coupon.setUpdateUser(BaseContext.getUserId());
        
        couponMapper.insert(coupon);
    }
    
    @Override
    public void update(CouponDTO couponDTO) {
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(couponDTO, coupon);
        
        coupon.setUpdateTime(LocalDateTime.now());
        coupon.setUpdateUser(BaseContext.getUserId());
        
        couponMapper.update(coupon);
    }
    
    @Override
    public void deleteById(Long id) {
        couponMapper.deleteById(id);
    }
    
    @Override
    public CouponVO getById(Long id) {
        Coupon coupon = couponMapper.getById(id);
        CouponVO couponVO = new CouponVO();
        BeanUtils.copyProperties(coupon, couponVO);
        return couponVO;
    }
    
    @Override
    public PageResult pageQuery(CouponPageQueryDTO couponPageQueryDTO) {
        PageHelper.startPage(couponPageQueryDTO.getPage(), couponPageQueryDTO.getPageSize());
        Page<Coupon> page = couponMapper.pageQuery(couponPageQueryDTO);
        
        List<CouponVO> list = page.getResult().stream().map(coupon -> {
            CouponVO couponVO = new CouponVO();
            BeanUtils.copyProperties(coupon, couponVO);
            return couponVO;
        }).collect(Collectors.toList());
        
        return new PageResult(page.getTotal(), list);
    }
    
    @Override
    public void startOrStop(Integer status, Long id) {
        Coupon coupon = Coupon.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getUserId())
                .build();
        couponMapper.update(coupon);
    }
    
    @Override
    public List<CouponVO> listAvailable(Long userId) {
        List<Coupon> coupons = couponMapper.listEnabled();
        
        return coupons.stream().map(coupon -> {
            CouponVO couponVO = new CouponVO();
            BeanUtils.copyProperties(coupon, couponVO);
            
            if (userId != null) {
                // 查询用户已领取数量
                int receivedCount = userCouponMapper.countByUserIdAndCouponId(userId, coupon.getId());
                couponVO.setReceivedCount(receivedCount);
                couponVO.setReceived(receivedCount >= coupon.getPerLimit());
            }
            
            return couponVO;
        }).collect(Collectors.toList());
    }
}
