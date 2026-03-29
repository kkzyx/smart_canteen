//package com.holly.service.impl;
//
//import com.holly.constant.StatusConstant;
//import com.holly.context.BaseContext;
//
//import com.holly.entity.PickupAddress;
//import com.holly.mapper.PickupAddressMapper;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
///**
// * @description
// */
//@Service
//@RequiredArgsConstructor
//public class PickupAddressServiceImpl implements PickupAddressService {
//
//  private final PickupAddressMapper pickupAddressMapper;
//
//  @Override
//  public List<PickupAddress> list(PickupAddress addressBook) {
//    // TODO: 获取地址列表未使用分页，等待完善
//    return pickupAddressMapper.list(addressBook);
//  }
//
//  @Override
//  public void save(AddressBook addressBook) {
//    addressBook.setUserId(BaseContext.getUserId());
//
//    // 当前新增地址是否设置了为默认地址
//    boolean isDefault = Optional.ofNullable(addressBook.getIsDefault())
//            .map(StatusConstant.ENABLE::equals)
//            .orElse(false);
//
//    addressBook.setIsDefault(0);
//
//    // 如果当前新增地址设置为默认地址，则将当前用户的所有地址修改为非默认地址
//    if(isDefault) {
//      pickupAddressMapper.updateIsDefaultByUserId(addressBook);
//      // 设置当前地址为默认地址
//      addressBook.setIsDefault(1);
//    }
//    pickupAddressMapper.insert(addressBook);
//  }
//
//  @Override
//  public AddressBook getById(Long id) {
//    return pickupAddressMapper.getById(id);
//  }
//
//  @Override
//  public void update(AddressBook addressBook) {
//    pickupAddressMapper.update(addressBook);
//  }
//
//  @Transactional
//  @Override
//  public void setDefault(AddressBook addressBook) {
//    /* 1、将当前用户的所有地址修改为非默认地址 */
//    addressBook.setIsDefault(0);
//    addressBook.setUserId(BaseContext.getUserId());
//    pickupAddressMapper.updateIsDefaultByUserId(addressBook);
//
//    /* 2、将当前地址改为默认地址 */
//    addressBook.setIsDefault(1);
//    pickupAddressMapper.update(addressBook);
//  }
//
//  @Override
//  public void deleteById(Long id) {
//    pickupAddressMapper.deleteById(id);
//  }
//}
