package com.holly.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @description
 */
@Data
@Schema(description = "微信小程序DTO")
public class UserDTO implements Serializable {

  @Schema(description = "微信用户id")
  private Long id;

  @Schema(description = "微信用户昵称")
  private String name;

  @Schema(description = "微信用户头像")
  private String avatar;
}

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
//@ApiModel(description = "微信小程序DTO")
//public class UserDTO implements Serializable {
//
//  @ApiModelProperty("微信用户id")
//  private Long id;
//
//  @ApiModelProperty("微信用户昵称")
//  private String name;
//
//  @ApiModelProperty("微信用户头像")
//  private String avatar;
//}
