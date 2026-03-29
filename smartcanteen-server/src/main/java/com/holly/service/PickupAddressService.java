package com.holly.service;

import com.holly.entity.PickupAddress;

import java.util.List;

/**
 * @description
 */
public interface PickupAddressService {
  
  /**
   * 查询地址簿列表
   * @param pickupaddress 条件查询参数
   * @return 地址簿列表
   */
  List<PickupAddress> list(PickupAddress pickupaddress);
  
  /**
   * 新增地址
   * @param pickupaddress 地址簿对象
   */
  void save(PickupAddress pickupaddress);
  
  /**
   * 根据id查询地址簿
   * @param id 地址簿id
   * @return 地址簿对象
   */
  PickupAddress getById(Long id);
  
  /**
   * 更新地址簿
   * @param pickupaddress 地址簿对象
   */
  void update(PickupAddress pickupaddress);
  
  /**
   * 设置默认地址簿
   * @param pickupaddress 地址簿对象
   */
  void setDefault(PickupAddress pickupaddress);
  
  /**
   * 根据地址簿id删除地址簿
   * @param id 地址簿id
   */
  void deleteById(Long id);
}
