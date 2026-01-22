package com.holly.controller.client;

import com.holly.dto.ShoppingCartDTO;
import com.holly.entity.ShoppingCart;
import com.holly.result.Result;
import com.holly.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description
 */
@Slf4j
@Tag(name = "C端购物车相关接口")
@RestController
@RequestMapping("/client/shoppingCart")
@RequiredArgsConstructor
public class ShoppingCartController {
  
  private final ShoppingCartService shoppingCartService;
  
  /**
   * 添加购物车
   * @param shoppingCartDTO 购物车DTO
   */
  @PostMapping("/add")
  @Operation(summary = "添加购物车")
  public Result<?> add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
    log.info("添加购物车，商品信息为==> {}", shoppingCartDTO);
    shoppingCartService.addShoppingCart(shoppingCartDTO);
    return Result.success();
  }
  
  /**
   * 查看购物车
   */
  @GetMapping("/list")
  @Operation(summary = "查看购物车")
  public Result<List<ShoppingCart>> list() {
    List<ShoppingCart> list = shoppingCartService.showShoppingCart();
    return Result.success(list);
  }
  
  /**
   * 更新购物车中商品的数量
   */
  @PutMapping("/update")
  @Operation(summary = "更新购物车中商品的数量")
  public Result<?> update(@RequestBody ShoppingCartDTO shoppingCartDTO) {
    log.info("更新购物车中商品的数量");
    shoppingCartService.updateShoppingCartNumber(shoppingCartDTO);
    return Result.success();
  }
  
  /**
   * 清空购物车
   */
  @DeleteMapping("/clean")
  @Operation(summary = "清空购物车")
  public Result<?> clean() {
    shoppingCartService.cleanShoppingCart();
    return Result.success();
  }
  
  /**
   * 删除购物车中一个商品
   * @param shoppingCartDTO 购物车DTO
   */
  @PostMapping("/sub")
  @Operation(summary = "删除购物车中一个商品")
  public Result<?> sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
    shoppingCartService.subShoppingCart(shoppingCartDTO);
    return Result.success();
  }
}
