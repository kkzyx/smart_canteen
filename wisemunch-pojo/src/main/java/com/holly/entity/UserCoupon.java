package com.holly.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户优惠券实体类
 */
@Data
@Builder
@ApiModel(description = "用户优惠券实体类")
@NoArgsConstructor
@AllArgsConstructor
public class UserCoupon implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 状态：未使用 */
    public static final Integer STATUS_UNUSED = 0;
    /** 状态：已使用 */
    public static final Integer STATUS_USED = 1;
    /** 状态：已过期 */
    public static final Integer STATUS_EXPIRED = 2;
    
    @ApiModelProperty("用户优惠券id")
    private Long id;
    
    @ApiModelProperty("用户id")
    private Long userId;
    
    @ApiModelProperty("优惠券id")
    private Long couponId;
    
    @ApiModelProperty("状态 0-未使用 1-已使用 2-已过期")
    private Integer status;
    
    @ApiModelProperty("使用的订单id")
    private Long orderId;
    
    @ApiModelProperty("领取时间")
    private LocalDateTime receiveTime;
    
    @ApiModelProperty("使用时间")
    private LocalDateTime useTime;
}
