package com.holly.controller.client;

import com.holly.context.BaseContext;
import com.holly.result.Result;
import com.holly.service.CouponService;
import com.holly.service.UserCouponService;
import com.holly.vo.CouponVO;
import com.holly.vo.UserCouponVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户端-优惠券接口
 */
@Slf4j
@Tag(name = "C端-优惠券接口")
@RestController("clientCouponController")
@RequestMapping("/client/coupon")
@RequiredArgsConstructor
public class CouponController {
    
    private final CouponService couponService;
    private final UserCouponService userCouponService;
    
    @GetMapping("/list")
    @Operation(summary = "查询可领取的优惠券列表")
    public Result<List<CouponVO>> list() {
        log.info("查询可领取的优惠券列表");
        Long userId = BaseContext.getUserId();
        List<CouponVO> list = couponService.listAvailable(userId);
        return Result.success(list);
    }
    
    @PostMapping("/receive/{couponId}")
    @Operation(summary = "领取优惠券")
    public Result<String> receive(@PathVariable Long couponId) {
        log.info("领取优惠券：{}", couponId);
        userCouponService.receive(couponId);
        return Result.success("领取成功");
    }
    
    @GetMapping("/my")
    @Operation(summary = "查询我的优惠券")
    public Result<List<UserCouponVO>> listMyCoupons(@RequestParam(required = false) Integer status) {
        log.info("查询我的优惠券，状态：{}", status);
        List<UserCouponVO> list = userCouponService.listMyCoupons(status);
        return Result.success(list);
    }
    
    @GetMapping("/available")
    @Operation(summary = "查询订单可用优惠券")
    public Result<List<UserCouponVO>> listAvailableForOrder(@RequestParam BigDecimal orderAmount) {
        log.info("查询订单可用优惠券，订单金额：{}", orderAmount);
        List<UserCouponVO> list = userCouponService.listAvailableForOrder(orderAmount);
        return Result.success(list);
    }
}
