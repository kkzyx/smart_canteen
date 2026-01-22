package com.holly.vo;

import com.holly.entity.SetmealDish;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @description 套餐总览
 */
@Data
@Builder
@Schema(description = "套餐总览")
@NoArgsConstructor
@AllArgsConstructor
public class SetmealVO implements Serializable {
  
  @Schema(description = "套餐id")
  private Long id;
  
  @Schema(description = "分类id")
  private Long categoryId;
  
  @Schema(description = "套餐名称")
  private String name;
  
  @Schema(description = "套餐价格")
  private BigDecimal price;
  
  @Schema(description = "状态 0:停用 1:启用")
  private Integer status;
  
  @Schema(description = "描述信息")
  private String description;
  
  @Schema(description = "图片")
  private String image;
  
  @Schema(description = "更新时间")
  private LocalDateTime updateTime;
  
  @Schema(description = "分类名称")
  private String categoryName;
  
  @Schema(description = "套餐和菜品的关联关系")
  private List<SetmealDish> setmealDishes = new ArrayList<>();
}
