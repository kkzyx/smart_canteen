package com.holly.service;

import com.holly.dto.ShopInfoDTO;
import com.holly.vo.ShopInfoVO;

/**
 * @description
 */
public interface ShopInfoService {
  
  /**
   * 获取商家店铺信息
   */
  ShopInfoVO getShopInfo();
  
  /**
   * 更新商家店铺信息
   * @param shopInfoDTO 商家店铺信息
   */
  void updateShopInfo(ShopInfoDTO shopInfoDTO);
}
