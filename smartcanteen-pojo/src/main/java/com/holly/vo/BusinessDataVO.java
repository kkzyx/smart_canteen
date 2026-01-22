package com.holly.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @description 工作台今日数据查询
 */
@Data
@Builder
@Schema(description = "工作台今日数据查询")
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDataVO implements Serializable {
  
  /** 营业额 */
  @Schema(description = "营业额")
  private Double turnover;
  
  /** 有效订单数 */
  @Schema(description = "有效订单数")
  private Integer validOrderCount;
  
  /** 订单完成率 */
  @Schema(description = "订单完成率")
  private Double orderCompletionRate;
  
  /** 平均客单价 */
  @Schema(description = "平均客单价")
  private Double unitPrice;
  
  /** 新增用户数 */
  @Schema(description = "新增用户数")
  private Integer newUsers;
}
