package com.holly.query;

import com.holly.query.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @description
 */
@Data
@Schema(description = "菜品分页查询数据传输对象")
public class DishPageQueryDTO extends BasePageQuery implements Serializable {
  
  @Schema(description = "菜品名称")
  private String name;
  
  @Schema(description = "该菜品所属分类id")
  private Integer categoryId;
  
  @Schema(description = "菜品状态 0-表示禁用 1-表示启用")
  private Integer status;
}
