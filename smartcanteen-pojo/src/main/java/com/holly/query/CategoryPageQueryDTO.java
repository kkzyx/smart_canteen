package com.holly.query;

import com.holly.query.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description
 */
@Data
@Builder
@Schema(description = "分类分页查询参数DTO")
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPageQueryDTO extends BasePageQuery implements Serializable {

  @Schema(description = "分类名称")
  private String name;
  
  @Schema(description = "分类类型 1-菜品分类")
  private Integer type;
  
  @Schema(description = "分类状态 1-启用 0-停用")
  private Integer status;
}
