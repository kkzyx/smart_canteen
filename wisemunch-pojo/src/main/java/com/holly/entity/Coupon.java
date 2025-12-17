package com.holly.entity;

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
 * 优惠券实体类
 */
@Data
@Builder
@ApiModel(description = "优惠券实体类")
@NoArgsConstructor
@AllArgsConstructor
public class Coupon implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 优惠券类型：满减券 */
    public static final Integer TYPE_FULL_REDUCTION = 1;
    /** 优惠券类型：折扣券 */
    public static final Integer TYPE_DISCOUNT = 2;
    /** 优惠券类型：无门槛券 */
    public static final Integer TYPE_NO_THRESHOLD = 3;
    
    /** 状态：禁用 */
    public static final Integer STATUS_DISABLED = 0;
    /** 状态：启用 */
    public static final Integer STATUS_ENABLED = 1;
    
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
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    
    @ApiModelProperty("创建人")
    private Long createUser;
    
    @ApiModelProperty("修改人")
    private Long updateUser;
}
