package com.holly.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "用户优惠券VO")
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponVO implements Serializable {
    
    @ApiModelProperty("用户优惠券id")
    private Long id;
    
    @ApiModelProperty("优惠券id")
    private Long couponId;
    
    @ApiModelProperty("优惠券名称")
    private String name;
    
    @ApiModelProperty("优惠券类型 1-满减券 2-折扣券 3-无门槛券")
    private Integer type;
    
    @ApiModelProperty("优惠金额")
    private BigDecimal discountAmount;
    
    @ApiModelProperty("折扣率(0.1-0.99)")
    private BigDecimal discountRate;
    
    @ApiModelProperty("使用门槛(满多少可用)")
    private BigDecimal minAmount;
    
    @ApiModelProperty("有效期开始时间")
    private LocalDateTime startTime;
    
    @ApiModelProperty("有效期结束时间")
    private LocalDateTime endTime;
    
    @ApiModelProperty("优惠券描述")
    private String description;
    
    @ApiModelProperty("状态 0-未使用 1-已使用 2-已过期")
    private Integer status;
    
    @ApiModelProperty("领取时间")
    private LocalDateTime receiveTime;
    
    @ApiModelProperty("使用时间")
    private LocalDateTime useTime;
    
    @ApiModelProperty("是否可用（根据订单金额计算）")
    private Boolean canUse;
}
