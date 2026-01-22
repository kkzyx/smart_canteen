package com.holly.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 优惠券分页查询DTO
 */
@Data
@Schema(description = "优惠券分页查询DTO")
public class CouponPageQueryDTO implements Serializable {
    
    @Schema(description = "页码")
    private int page = 1;
    
    @Schema(description = "每页记录数")
    private int pageSize = 10;
    
    @Schema(description = "优惠券名称")
    private String name;
    
    @Schema(description = "优惠券类型")
    private Integer type;
    
    @Schema(description = "状态")
    private Integer status;
}
