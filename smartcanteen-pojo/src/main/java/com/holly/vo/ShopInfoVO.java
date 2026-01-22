package com.holly.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description
 */
@Data
@Builder
@Schema(description = "商家信息VO")
@NoArgsConstructor
@AllArgsConstructor
public class ShopInfoVO implements Serializable {
  
  @Schema(description = "商家名称")
  private String name;
  @Schema(description = "商家LOGO")
  private String logo;
  @Schema(description = "商家地址")
  private String address;
  @Schema(description = "营业时间")
  private String businessHours;
  @Schema(description = "联系电话")
  private String contactPhone;
  @Schema(description = "营业状态 1营业 0休息")
  private Integer status;
  
  @Schema(description = "经度")
  private String longitude;
  
  @Schema(description = "纬度")
  private String latitude;
  
  @Schema(description = "配送范围（公里）")
  private Integer deliveryRange;
  
  @Schema(description = "起送价")
  private BigDecimal minimumOrder;
  
  @Schema(description = "配送费")
  private BigDecimal deliveryFee;
}
