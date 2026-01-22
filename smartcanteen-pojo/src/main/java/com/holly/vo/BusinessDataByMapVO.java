package com.holly.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

/**
 * @description 运营数据报表-明细数据导出excel业务数据
 */
@Data
@Builder
@Schema(description = "运营数据报表-明细数据导出excel业务数据")
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDataByMapVO {
  
  /** 营业额 */
  @Schema(description = "营业额")
  private Map<LocalDate, Double> turnoverMap;
  
  /** 有效订单数 */
  @Schema(description = "有效订单数")
  private Map<LocalDate, Long> validOrderCountMap;
  
  /** 订单完成率 */
  @Schema(description = "订单完成率")
  private Map<LocalDate, Double> orderCompletionRateMap;
  
  /** 平均客单价 */
  @Schema(description = "平均客单价")
  private Map<LocalDate, Double> unitPriceMap;
  
  /** 新增用户数 */
  @Schema(description = "新增用户数")
  private Map<LocalDate, Long> newUsersMap;
}
