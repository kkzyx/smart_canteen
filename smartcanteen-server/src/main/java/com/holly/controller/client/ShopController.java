package com.holly.controller.client;

import com.holly.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @description
 */
@Slf4j
@Tag(name = "店铺相关接口")
@RestController("clientShopController")
@RequestMapping("/client/shop")
@RequiredArgsConstructor
public class ShopController {
  
  private static final String KEY = "SHOP_STATUS";
  
  private final RedisTemplate<String, String> redisTemplate;
  
  /**
   * 获取店铺的营业状态
   */
  @GetMapping("/status")
  @Operation(summary = "获取店铺的营业状态")
  public Result<Integer> getStatus() {
    int status = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue()
                                                                 .get(KEY)));
    log.info("获取到店铺的营业状态为：{}", status == 1 ? "营业中" : "打烊中");
    return Result.success(status);
  }
}
