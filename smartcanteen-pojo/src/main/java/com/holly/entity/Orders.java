package com.holly.entity;

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
@Schema(description = "订单实体类")
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  /** 订单状态：1待付款 */
  public static final Integer PENDING_PAYMENT = 1;
  /** 订单状态：2待接单 */
  public static final Integer TO_BE_CONFIRMED = 2;
  /** 订单状态：3已接单 */
  public static final Integer CONFIRMED = 3;
  /** 订单状态：4派送中 */
  public static final Integer DELIVERY_IN_PROGRESS = 4;
  /** 订单状态：5已完成 */
  public static final Integer COMPLETED = 5;
  /** 订单状态：6已取消 */
  public static final Integer CANCELLED = 6;
  /** 订单状态：7退款 */
  public static final Integer REFUNDING = 7;
  /** 订单状态：8堂食订单 */
  public static final Integer DINE_IN_ORDER = 8;
  
  
  /** 支付状态：0未支付 */
  public static final Integer UN_PAID = 0;
  /** 支付状态：1已支付 */
  public static final Integer PAID = 1;
  /** 支付状态：2退款 */
  public static final Integer REFUND = 2;
  /** 订单类型：1外卖 */
  public static final Integer TAKEAWAY = 1;
  /** 订单类型：2堂食 */
  public static final Integer DINE_IN = 2;
  
  
  @Schema(description = "订单id")
  private Long id;
  
  @Schema(description = "订单号")
  private String number;
  
  @Schema(description = "订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款")
  private Integer status;
  
  @Schema(description = "下单用户id")
  private Long userId;
  
  @Schema(description = "地址id")
  private Long addressBookId;
  
  @Schema(description = "下单时间")
  // @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 局部字段日期格式化
  private LocalDateTime orderTime;
  
  @Schema(description = "结账时间")
  private LocalDateTime checkoutTime;
  
  @Schema(description = "支付方式 1微信，2支付宝")
  private Integer payMethod;
  
  @Schema(description = "支付状态 0未支付 1已支付 2退款")
  private Integer payStatus;
  
  @Schema(description = "实收金额")
  private BigDecimal amount;
  
  @Schema(description = "备注")
  private String remark;
  
  @Schema(description = "用户名")
  private String userName;
  
  @Schema(description = "手机号")
  private String phone;
  
  @Schema(description = "地址")
  private String address;
  
  @Schema(description = "收货人")
  private String consignee;
  
  @Schema(description = "订单取消原因")
  private String cancelReason;
  
  @Schema(description = "订单拒绝原因")
  private String rejectionReason;
  
  @Schema(description = "订单取消时间")
  private LocalDateTime cancelTime;
  
  @Schema(description = "预计送达时间")
  private LocalDateTime estimatedDeliveryTime;
  
  @Schema(description = "配送状态  1立即送出  0选择具体时间")
  private Integer deliveryStatus;
  
  @Schema(description = "送达时间")
  private LocalDateTime deliveryTime;
  
  @Schema(description = "打包费")
  private int packAmount;
  
  @Schema(description = "餐具数量")
  private int tablewareNumber;
  
  @Schema(description = "餐具数量状态  1按餐量提供  0选择具体数量")
  private Integer tablewareStatus;
  
  @Schema(description = "订单类型 1外卖 2堂食")
  private Integer orderType;
  
  @Schema(description = "取餐号（堂食专用）")
  private String pickupNumber;

  @Schema(description = "是否评价 0否 1是")
  private Short isComment;

  @Schema(description = "使用的用户优惠券id")
  private Long userCouponId;

  @Schema(description = "优惠券优惠金额")
  private BigDecimal couponAmount;
}
