package com.holly.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 优惠券分页查询DTO
 */
@Data
@ApiModel(description = "优惠券分页查询DTO")
public class CouponPageQueryDTO implements Serializable {
    
    @ApiModelProperty("页码")
    private int page = 1;
    
    @ApiModelProperty("每页记录数")
    private int pageSize = 10;
    
    @ApiModelProperty("优惠券名称")
    private String name;
    
    @ApiModelProperty("优惠券类型")
    private Integer type;
    
    @ApiModelProperty("状态")
    private Integer status;
}
