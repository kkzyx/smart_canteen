package com.holly.vo;

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
@Schema(description = "微信登录数据展示对象")
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO implements Serializable {
  
  @Schema(description = "新增用户id")
  private Long id;
  
  @Schema(description = "openid用户唯一标识")
  private String openid;
  
  @Schema(description = "jwt校验令牌token")
  private String token;
}
