package com.holly.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@Schema(description = "敏感词分页查询参数DTO")
@NoArgsConstructor
@AllArgsConstructor
public class SensitivePageQueryDTO extends BasePageQuery implements Serializable {

    @Schema(description = "敏感词")
    private String sensitive;
}
