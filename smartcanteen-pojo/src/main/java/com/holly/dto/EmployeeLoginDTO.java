package com.holly.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @description
 */
@Data
@Schema(description = "员工登录DTO")
public class EmployeeLoginDTO implements Serializable {
  
  @Schema(description = "用户名")
  private String username;
  
  @Schema(description = "密码")
  private String password;
}
