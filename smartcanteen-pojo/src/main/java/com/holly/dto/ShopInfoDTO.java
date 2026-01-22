package com.holly.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @description
 */
@Data
@Schema(description = "商家信息DTO")
public class ShopInfoDTO implements Serializable {

  @Schema(description = "商家名称", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "商家名称不能为空")
  private String name;

  @Schema(description = "商家LOGO")
  private String logo;

  @Schema(description = "详细地址", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "地址不能为空")
  private String address;

  @Schema(description = "营业时间（格式：08:00-22:00）", example = "08:00-22:00")
  @Pattern(regexp = "\\d{2}:\\d{2}-\\d{2}:\\d{2}", message = "营业时间格式不正确")
  private String businessHours;

  @Schema(description = "联系电话", example = "13800138000")
  @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
  private String contactPhone;

  @Schema(description = "营业状态 1营业 0休息", example = "1")
  private Integer status;
}

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Pattern;
//import java.io.Serializable;
//
///**
// * @description
// */
//@Data
//@ApiModel("商家信息DTO")
//public class ShopInfoDTO implements Serializable {
//
//  @ApiModelProperty("商家名称")
//  @NotBlank(message = "商家名称不能为空")
//  private String name;
//
//  @ApiModelProperty("商家LOGO")
//  private String logo;
//
//  @ApiModelProperty("详细地址")
//  @NotBlank(message = "地址不能为空")
//  private String address;
//
//  @ApiModelProperty("营业时间（格式：08:00-22:00）")
//  @Pattern(regexp = "\\d{2}:\\d{2}-\\d{2}:\\d{2}", message = "营业时间格式不正确")
//  private String businessHours;
//
//  @ApiModelProperty("联系电话")
//  @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
//  private String contactPhone;
//
//  @ApiModelProperty("营业状态 1营业 0休息")
//  private Integer status;
//}
