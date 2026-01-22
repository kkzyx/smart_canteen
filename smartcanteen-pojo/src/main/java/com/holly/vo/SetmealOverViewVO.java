package com.holly.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description 套餐总览
 */
@Data
@Builder
@Schema(description = "查询套餐总览")
@NoArgsConstructor
@AllArgsConstructor
public class SetmealOverViewVO implements Serializable {
  
  /** 已启售数量 */
  @Schema(description = "已启售数量")
  private long sold;
  
  /** 已停售数量 */
  @Schema(description = "已停售数量")
  private long discontinued;
}
