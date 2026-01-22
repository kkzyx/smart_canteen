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
 * @description
 */
@Data
@Builder
@Schema(description = "菜品")
@NoArgsConstructor
@AllArgsConstructor
public class Dish implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Schema(description = "菜品id")
  private Long id;
  
  @Schema(description = "菜品名称")
  private String name;
  
  @Schema(description = "菜品分类id")
  private Long categoryId;
  
  @Schema(description = "菜品价格")
  private BigDecimal price;
  
  @Schema(description = "图片url")
  private String image;
  
  @Schema(description = "描述信息")
  private String description;
  
  @Schema(description = "菜品状态：0-停售、1-起售")
  private Integer status;

  /**
   * 用户每购买一次这个菜品，热度加1
   */
  @Schema(description = "菜品热度")
  private Long hotSpot;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updateTime;
  
  private Long createUser;
  
  private Long updateUser;
}
