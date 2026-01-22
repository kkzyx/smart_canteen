package com.holly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySequence implements Serializable {
  
  private static final long serialVersionUID = 1L;
  @Schema(description = "日期")
  private LocalDate date;
  @Schema(description = "字母")
  private Character letter;
  @Schema(description = "数字")
  private Integer number;
  @Schema(description = "版本")
  private Integer version;
  
  public DailySequence(LocalDate date, Character letter, Integer number) {
    this(date, letter, number, 0);
  }
  
}
