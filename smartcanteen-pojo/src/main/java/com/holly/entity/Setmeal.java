package com.holly.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @description 套餐
 */
@Data
@Builder
@Schema(description = "套餐实体类")
@NoArgsConstructor
@AllArgsConstructor
public class Setmeal implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  @Schema(description = "套餐id")
  private Long id;
  
  @Schema(description = "分类id")
  private Long categoryId;

  @Schema(description = "分类名称")
  private String categoryName;
  
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

  /**
   * 用户每购买一次这个套餐，热度加1
   */
  @Schema(description = "套餐热度")
  private Long hotSpot;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updateTime;
  
  private Long createUser;
  
  private Long updateUser;
}
