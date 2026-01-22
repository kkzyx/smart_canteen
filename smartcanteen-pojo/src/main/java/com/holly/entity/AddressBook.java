package com.holly.entity;

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
@Schema(description = "地址簿实体类")
@NoArgsConstructor
@AllArgsConstructor
public class AddressBook implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Schema(description = "地址簿id")
  private Long id;
  
  @Schema(description = "用户id")
  private Long userId;
  
  @Schema(description = "收货人")
  private String consignee;
  
  @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
  private String phone;
  
  @Schema(description = "性别 0 女 1 男")
  private String sex;
  
  @Schema(description = "省级区划编号")
  private String provinceCode;
  
  @Schema(description = "省级名称")
  private String provinceName;
  
  @Schema(description = "市级区划编号")
  private String cityCode;
  
  @Schema(description = "市级名称")
  private String cityName;
  
  @Schema(description = "区级区划编号")
  private String districtCode;
  
  @Schema(description = "区级名称")
  private String districtName;

  /*
    * 新增变量，schoolCode、schoolName、campusCode、campusName、dormitoryCode、dormitoryName、roomNum
   */
  @Schema(description = "学校编码")
  private String schoolCode;
  @Schema(description = "学校名称")
  private String schoolName;

  @Schema(description = "校区编码")
  private String campusCode;
  @Schema(description = "校区名称")
  private String campusName;

  @Schema(description = "宿舍楼编码")
  private String dormitoryCode;
  @Schema(description = "宿舍楼名称")
  private String dormitoryName;

  @Schema(description = "房间号")
  private String roomNum;
  
  @Schema(description = "详细地址", requiredMode = Schema.RequiredMode.REQUIRED)
  private String detail;
  
  @Schema(description = "标签")
  private String label;
  
  @Schema(description = "是否为默认地址 0否 1是")
  private Integer isDefault;
}
