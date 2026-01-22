package com.holly.controller.store;

import com.holly.dto.ShopInfoDTO;
import com.holly.result.Result;
import com.holly.service.ShopInfoService;
import com.holly.vo.ShopInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @description
 */
@Slf4j
@RestController("storeShopInfoController")
@Tag(name = "管理端-商家管理接口")
@RequestMapping("/store/shopInfo")
@RequiredArgsConstructor
public class ShopInfoController {
  private final ShopInfoService shopInfoService;
  
  @GetMapping
  @Operation(summary = "获取商家信息")
  public Result<ShopInfoVO> getShopInfo() {
    return Result.success(shopInfoService.getShopInfo());
  }
  
  @PutMapping
  @Operation(summary = "更新商家信息")
  public Result<?> updateShopInfo(@Valid @RequestBody ShopInfoDTO shopInfoDTO) {
    shopInfoService.updateShopInfo(shopInfoDTO);
    return Result.success();
  }
}
