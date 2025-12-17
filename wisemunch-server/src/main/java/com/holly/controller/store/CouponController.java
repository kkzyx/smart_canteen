package com.holly.controller.store;

import com.holly.dto.CouponDTO;
import com.holly.dto.CouponPageQueryDTO;
import com.holly.result.PageResult;
import com.holly.result.Result;
import com.holly.service.CouponService;
import com.holly.vo.CouponVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端-优惠券管理接口
 */
@Slf4j
@Api(tags = "管理端-优惠券管理接口")
@RestController("storeCouponController")
@RequestMapping("/store/coupon")
@RequiredArgsConstructor
public class CouponController {
    
    private final CouponService couponService;
    
    @PostMapping
    @ApiOperation("新增优惠券")
    public Result<String> save(@RequestBody CouponDTO couponDTO) {
        log.info("新增优惠券：{}", couponDTO);
        couponService.save(couponDTO);
        return Result.success();
    }
    
    @PutMapping
    @ApiOperation("修改优惠券")
    public Result<String> update(@RequestBody CouponDTO couponDTO) {
        log.info("修改优惠券：{}", couponDTO);
        couponService.update(couponDTO);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation("删除优惠券")
    public Result<String> delete(@PathVariable Long id) {
        log.info("删除优惠券：{}", id);
        couponService.deleteById(id);
        return Result.success();
    }
    
    @GetMapping("/{id}")
    @ApiOperation("根据id查询优惠券")
    public Result<CouponVO> getById(@PathVariable Long id) {
        log.info("根据id查询优惠券：{}", id);
        CouponVO couponVO = couponService.getById(id);
        return Result.success(couponVO);
    }
    
    @GetMapping("/page")
    @ApiOperation("分页查询优惠券")
    public Result<PageResult> page(CouponPageQueryDTO couponPageQueryDTO) {
        log.info("分页查询优惠券：{}", couponPageQueryDTO);
        PageResult pageResult = couponService.pageQuery(couponPageQueryDTO);
        return Result.success(pageResult);
    }
    
    @PostMapping("/status/{status}")
    @ApiOperation("启用/禁用优惠券")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        log.info("启用/禁用优惠券：status={}, id={}", status, id);
        couponService.startOrStop(status, id);
        return Result.success();
    }
}
