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
@Schema(description = "小程序端用户实体信息")
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Schema(description = "用户id")
  private Long id;
  
  @Schema(description = "微信用户唯一标识")
  private String openid;
  
  @Schema(description = "微信用户姓名")
  private String name;
  
  @Schema(description = "手机号")
  private String phone;
  
  @Schema(description = "性别 0 女 1 男")
  private String sex;
  
  @Schema(description = "头像")
  private String avatar;
  
  @Schema(description = "注册时间")
  private LocalDateTime createTime;
}
