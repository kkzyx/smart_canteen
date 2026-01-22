package com.holly.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description 销量TOP10视图对象
 */
@Data
@Builder
@Schema(description = "销量TOP10视图对象")
@NoArgsConstructor
@AllArgsConstructor
public class SalesTop10ReportVO implements Serializable {
  
  /** 商品名称列表，以逗号分隔，例如：鱼香肉丝,宫保鸡丁,水煮鱼 */
  @Schema(description = "商品名称列表，以逗号分隔", example = "鱼香肉丝,宫保鸡丁,水煮鱼")
  private String nameList;
  
  /** 销量列表，以逗号分隔，例如：260,215,200 */
  @Schema(description = "销量列表，以逗号分隔", example = "260,215,200")
  private String numberList;
}
