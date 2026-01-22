package com.holly.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @description
 */
@Data
@Schema(description = "微信小程序用户登录DTO")
public class UserLoginDTO implements Serializable {

  @Schema(description = "微信授权码")
  private String code;
}

//
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.Data;
//
//import java.io.Serializable;
//
///**
// * @description
// */
//
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//import java.io.Serializable;
//
///**
// * @description
// */
//@Data
//@ApiModel(description = "微信小程序用户登录DTO")
//public class UserLoginDTO implements Serializable {
//
//  @ApiModelProperty("微信授权码")
//  private String code;
//}
