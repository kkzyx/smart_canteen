package com.holly.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class BasePageQuery implements Serializable {

    @Schema(description = "页码")
    private int page;

    @Schema(description = "每页记录数")
    private int pageSize;
}
