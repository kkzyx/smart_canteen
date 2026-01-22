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
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  @Schema(description = "分类id")
  private Long id;
  
  @Schema(description = "类型: 1菜品分类 2 套餐分类")
  private Integer type;
  
  @Schema(description = "分类名称")
  private String name;
  
  @Schema(description = "排序")
  private Integer sort;
  
  @Schema(description = "分类状态 0标识禁用 1表示启用")
  private Integer status;
  
  @Schema(description = "创建时间")
  private LocalDateTime createTime;
  
  @Schema(description = "更新时间")
  private LocalDateTime updateTime;
  
  @Schema(description = "创建人")
  private Long createUser;
  
  @Schema(description = "修改人")
  private Long updateUser;
}
