package com.holly.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.holly.entity.DishFlavor;
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
 * @description
 */
@Data
@Builder
@Schema(description = "菜品视图展示对象")
@NoArgsConstructor
@AllArgsConstructor
public class DishVO implements Serializable {
  
  @Schema(description = "菜品id")
  private Long id;
  
  @Schema(description = "菜品名称")
  private String name;
  
  @Schema(description = "菜品分类id")
  private Long categoryId;
  
  @Schema(description = "菜品价格")
  private BigDecimal price;
  
  @Schema(description = "菜品封面图片")
  private String image;
  
  @Schema(description = "菜品描述信息")
  private String description;
  
  @Schema(description = "菜品状态：0 停售 1 起售")
  private Integer status;

  @Schema(description = "热度")
  private Long hotSpot;

  @Schema(description = "分类名称")
  private String categoryName;

  @Schema(description = "更新时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updateTime;

  @Schema(description = "菜品关联的口味")
  private List<DishFlavor> flavors = new ArrayList<>();
}
