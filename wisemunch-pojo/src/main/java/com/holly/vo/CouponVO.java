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
 * 优惠券VO
 */
@Data
@Builder
@ApiModel(description = "优惠券VO")
@NoArgsConstructor
@AllArgsConstructor
public class CouponVO implements Serializable {
    
    @ApiModelProperty("优惠券id")
    private Long id;
    
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
    
    @ApiModelProperty("发行总量")
    private Integer totalCount;
    
    @ApiModelProperty("剩余数量")
    private Integer remainCount;
    
    @ApiModelProperty("每人限领数量")
    private Integer perLimit;
    
    @ApiModelProperty("有效期开始时间")
    private LocalDateTime startTime;
    
    @ApiModelProperty("有效期结束时间")
    private LocalDateTime endTime;
    
    @ApiModelProperty("状态 0-禁用 1-启用")
    private Integer status;
    
    @ApiModelProperty("优惠券描述")
    private String description;
    
    @ApiModelProperty("是否已领取（用户端用）")
    private Boolean received;
    
    @ApiModelProperty("已领取数量（用户端用）")
    private Integer receivedCount;
}
