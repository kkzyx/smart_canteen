package com.holly.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "评论数据传输对象")
public class CommentDTO {

    @Schema(description = "菜品id")
    private Long dishId;

    @Schema(description = "套餐id")
    private Long setmealId;

    @Schema(description = "订单号")
    private List<String> orderNumber;

    @Schema(description = "分页条件最小时间")
    private Date minDate;

    @Schema(description = "分页参数")
    private Integer size;
}
