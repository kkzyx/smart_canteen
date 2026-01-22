package com.holly.controller.store;

import com.holly.result.Result;
import com.holly.service.WorkspaceService;
import com.holly.vo.BusinessDataVO;
import com.holly.vo.DishOverViewVO;
import com.holly.vo.OrderOverViewVO;
import com.holly.vo.SetmealOverViewVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @description
 */
@Slf4j
@Tag(name = "工作台相关接口")
@RestController
@RequestMapping("/store/workspace")
@RequiredArgsConstructor
public class WorkSpaceController {
  
  private final WorkspaceService workspaceService;
  
  /**
   * 工作台今日数据查询
   */
  @GetMapping("/businessData")
  @Operation(summary = "工作台今日数据查询")
  public Result<BusinessDataVO> businessData() {
    // 今天的开始时间，例如：2024-08-02 00:00:00
    LocalDateTime begin = LocalDateTime.now()
            .with(LocalTime.MIN);
    // 今天的结束时间，例如：2024-08-02 23:59:59
    LocalDateTime end = LocalDateTime.now()
            .with(LocalTime.MAX);
    
    BusinessDataVO businessDataVO = workspaceService.getBusinessData(begin, end);
    return Result.success(businessDataVO);
  }
  
  /**
   * 订单管理数据查询
   */
  @GetMapping("/overviewOrders")
  @Operation(summary = "查询订单管理数据")
  public Result<OrderOverViewVO> orderOverView() {
    OrderOverViewVO orderOverView = workspaceService.getOrderOverView();
    return Result.success(orderOverView);
  }
  
  /**
   * 菜品管理数据查询
   */
  @GetMapping("/overviewDishes")
  @Operation(summary = "查询菜品总览")
  public Result<DishOverViewVO> dishOverView() {
    DishOverViewVO dishOverView = workspaceService.getDishOverView();
    return Result.success(dishOverView);
  }
  
  /**
   * 套餐管理数据查询
   */
  @GetMapping("/overviewSetmeals")
  @Operation(summary = "查询套餐总览")
  public Result<SetmealOverViewVO> setmealOverView() {
    SetmealOverViewVO setmealOverView = workspaceService.getSetmealOverView();
    return Result.success(setmealOverView);
  }
}
