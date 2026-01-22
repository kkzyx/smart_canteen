package com.holly.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "评论分页查询数据传输对象")
public class CommentPageQueryDTO extends BasePageQuery implements Serializable {
    @Schema(description = "菜品id")
    private Integer dishId;

    @Schema(description = "套餐id")
    private Integer setmealId;
}
