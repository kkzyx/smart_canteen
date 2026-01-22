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
@Schema(description = "商家信息实体类")
@NoArgsConstructor
@AllArgsConstructor
public class ShopInfo implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  @Schema(description = "商家信息实体id")
  private Long id;
  @Schema(description = "店家名称")
  private String name;
  @Schema(description = "logo")
  private String logo;
  @Schema(description = "店家地址")
  private String address;
  @Schema(description = "营业时间")
  private String businessHours;
  @Schema(description = "联系电话")
  private String contactPhone;
  @Schema(description = "1营业 0休息")
  private Integer status;
  @Schema(description = "创建时间")
  private LocalDateTime createTime;
  @Schema(description = "更新时间")
  private LocalDateTime updateTime;
}
