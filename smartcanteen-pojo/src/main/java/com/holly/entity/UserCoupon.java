package com.holly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "用户优惠券实体类")
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
    
    @Schema(description = "用户优惠券id")
    private Long id;
    
    @Schema(description = "用户id")
    private Long userId;
    
    @Schema(description = "优惠券id")
    private Long couponId;
    
    @Schema(description = "状态 0-未使用 1-已使用 2-已过期")
    private Integer status;
    
    @Schema(description = "使用的订单id")
    private Long orderId;
    
    @Schema(description = "领取时间")
    private LocalDateTime receiveTime;
    
    @Schema(description = "使用时间")
    private LocalDateTime useTime;
}
