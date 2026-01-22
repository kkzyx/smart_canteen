package com.holly.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户优惠券VO
 */
@Data
@Builder
@Schema(description = "用户优惠券VO")
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponVO implements Serializable {
    
    @Schema(description = "用户优惠券id")
    private Long id;
    
    @Schema(description = "优惠券id")
    private Long couponId;
    
    @Schema(description = "优惠券名称")
    private String name;
    
    @Schema(description = "优惠券类型 1-满减券 2-折扣券 3-无门槛券")
    private Integer type;
    
    @Schema(description = "优惠金额")
    private BigDecimal discountAmount;
    
    @Schema(description = "折扣率(0.1-0.99)")
    private BigDecimal discountRate;
    
    @Schema(description = "使用门槛(满多少可用)")
    private BigDecimal minAmount;
    
    @Schema(description = "有效期开始时间")
    private LocalDateTime startTime;
    
    @Schema(description = "有效期结束时间")
    private LocalDateTime endTime;
    
    @Schema(description = "优惠券描述")
    private String description;
    
    @Schema(description = "状态 0-未使用 1-已使用 2-已过期")
    private Integer status;
    
    @Schema(description = "领取时间")
    private LocalDateTime receiveTime;
    
    @Schema(description = "使用时间")
    private LocalDateTime useTime;
    
    @Schema(description = "是否可用（根据订单金额计算）")
    private Boolean canUse;
}
