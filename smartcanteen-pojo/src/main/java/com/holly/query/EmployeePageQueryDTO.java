package com.holly.query;

import com.holly.query.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @description
 */
@Data
@Schema(description = "员工分页查询DTO")
public class EmployeePageQueryDTO extends BasePageQuery implements Serializable {
  
  @Schema(description = "员工姓名")
  private String name;
}
