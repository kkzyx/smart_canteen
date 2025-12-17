package com.holly.mapper;

import com.holly.annotation.AutoFill;
import com.holly.entity.ShopInfo;
import com.holly.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @description
 */
@Mapper
public interface ShopInfoMapper {
  
  @Select("SELECT * FROM `shop_info` LIMIT 1")
  ShopInfo selectDefaultShop();
  
  /**
   * 更新店铺信息
   * @param shopInfo 店铺信息
   */
  @AutoFill(OperationType.UPDATE)
  void update(ShopInfo shopInfo);
}
