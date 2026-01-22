package com.holly.controller.client;

import com.holly.context.BaseContext;
import com.holly.entity.AddressBook;
import com.holly.result.Result;
import com.holly.service.AddressBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description
 */
@RestController
@RequestMapping("/client/addressBook")
@Tag(name = "C端地址簿接口")
@RequiredArgsConstructor
public class AddressBookController {
  
  private final AddressBookService addressBookService;
  
  /**
   * 查询当前登录用户的所有地址信息
   */
  @GetMapping("/list")
  @Operation(summary = "查询当前登录用户的所有地址信息")
  public Result<List<AddressBook>> list() {
    Long userId = BaseContext.getUserId();
    // 测试期间给固定值
//    userId = userId != null ? userId : 1L;
    AddressBook addressBook = AddressBook.builder()
            .userId(userId)
            .build();
    List<AddressBook> list = addressBookService.list(addressBook);
    return Result.success(list);
  }
  
  /**
   * 新增地址
   * @param addressBook 地址实体类信息
   */
  @PostMapping
  @Operation(summary = "新增地址")
  public Result<?> save(@RequestBody AddressBook addressBook) {
    addressBookService.save(addressBook);
    return Result.success();
  }
  
  /**
   * 根据id查询地址
   * @param id 地址簿id
   */
  @GetMapping
  @Operation(summary = "根据id查询地址")
  public Result<AddressBook> getById(@RequestParam Long id) {
    AddressBook addressBook = addressBookService.getById(id);
    return Result.success(addressBook);
  }
  
  /**
   * 根据id修改地址
   * @param addressBook 地址实体类信息
   */
  @PutMapping
  @Operation(summary = "根据id修改地址")
  public Result<?> update(@RequestBody AddressBook addressBook) {
    addressBookService.update(addressBook);
    return Result.success();
  }
  
  /**
   * 设置默认地址
   * @param addressBook 地址实体类信息
   */
  @PutMapping("/default")
  @Operation(summary = "设置默认地址")
  public Result<?> setDefault(@RequestBody AddressBook addressBook) {
    addressBookService.setDefault(addressBook);
    return Result.success();
  }
  
  /**
   * 根据id删除地址
   * @param id 地址簿id
   */
  @DeleteMapping("/{id}")
  @Operation(summary = "根据id删除地址")
  public Result<?> deleteById(@PathVariable Long id) {
    addressBookService.deleteById(id);
    return Result.success();
  }
  
  /**
   * 查询默认地址
   */
  @GetMapping("/default")
  @Operation(summary = "查询默认地址")
  public Result<AddressBook> getDefault() {
    Long userId = BaseContext.getUserId();
    userId = userId != null ? userId : 1L;
    AddressBook addressBook = AddressBook.builder()
            .isDefault(1)
            .userId(userId)
            .build();
    
    List<AddressBook> list = addressBookService.list(addressBook);
    
    if (list != null && list.size() == 1) {
      return Result.success(list.get(0));
    }
    
    return Result.error("没有查询到默认地址");
  }
}
