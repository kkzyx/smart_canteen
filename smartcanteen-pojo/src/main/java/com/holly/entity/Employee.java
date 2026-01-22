package com.holly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description
 */
@Data
@Builder
@Schema(description = "员工实体类")
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  @Schema(description = "员工id")
  private Long id;
  
  @Schema(description = "员工用户名")
  private String username;
  
  @Schema(description = "员工姓名")
  private String name;
  
  @Schema(description = "员工密码")
  private String password;
  
  @Schema(description = "员工手机号码")
  private String phone;
  
  @Schema(description = "员工性别")
  private String sex;
  
  @Schema(description = "是否启动该员工账号，0-禁用，1-启用")
  private Integer status;
  
  @Schema(description = "员工创建时间")
  private LocalDateTime createTime;
  
  @Schema(description = "员工更新时间")
  private LocalDateTime updateTime;
  
  @Schema(description = "员工创建者id")
  private Long createUser;
  
  @Schema(description = "员工更新者id")
  private Long updateUser;
}
