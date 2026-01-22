package com.holly.query;

import com.holly.query.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "订单分页查询参数")
public class OrdersPageQueryDTO extends BasePageQuery implements Serializable {
  
  @Schema(description = "订单号")
  private String number;
  
  @Schema(description = "手机号")
  private String phone;
  
  @Schema(description = "订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消")
  private Integer status;
  
  @Schema(description = "开始时间")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime beginTime;
  
  @Schema(description = "结束时间")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime endTime;
  
  @Schema(description = "用户id")
  private Long userId;
}
