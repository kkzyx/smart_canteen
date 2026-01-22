package com.holly.query;

import com.holly.query.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 套餐分页查询条件
 */
@Data
public class SetmealPageQueryDTO extends BasePageQuery implements Serializable {
  @Schema(description = "查询套餐名称")
  private String name;
  
  @Schema(description = "分类id")
  private Integer categoryId;
  
  @Schema(description = "状态：0-禁用 1-启用")
  private Integer status;
  
}
