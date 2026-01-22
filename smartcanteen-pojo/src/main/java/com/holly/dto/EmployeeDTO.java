package com.holly.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @description
 */
@Data
@Schema(description = "员工DTO")
public class EmployeeDTO implements Serializable {
  
  @Schema(description = "员工id")
  private Long id;
  
  @Schema(description = "用户名")
  private String username;
  
  @Schema(description = "姓名")
  private String name;
  
  @Schema(description = "手机号码")
  private String phone;
  
  @Schema(description = "性别")
  private String sex;
}
