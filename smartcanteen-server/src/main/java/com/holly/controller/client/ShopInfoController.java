package com.holly.controller.client;

import com.holly.result.Result;
import com.holly.service.ShopInfoService;
import com.holly.vo.ShopInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description
 */
@Slf4j
@RestController
@Tag(name = "C端-商家信息接口")
@RequestMapping("/client/shopInfo")
@RequiredArgsConstructor
public class ShopInfoController {
  private final ShopInfoService shopInfoService;
  
  @GetMapping
  @Operation(summary = "获取商家信息")
  public Result<ShopInfoVO> getShopInfo() {
    return Result.success(shopInfoService.getShopInfo());
  }
}
