package com.holly.service.impl;

import com.holly.constant.StatusConstant;
import com.holly.context.BaseContext;
import com.holly.entity.AddressBook;
import com.holly.mapper.AddressBookMapper;

import com.holly.service.AddressBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @description
 */
@Service
@RequiredArgsConstructor
public class AddressBookServiceImpl implements AddressBookService {
  
  private final AddressBookMapper addressBookMapper;
  
  @Override
  public List<AddressBook> list(AddressBook addressBook) {
    // TODO: 获取地址列表分页前端后mapperxml待修改
    return addressBookMapper.list(addressBook);
  }
  
  @Override
  public void save(AddressBook addressBook) {
//    addressBook.setUserId(BaseContext.getUserId());
    
    // 当前新增地址是否设置了为默认地址
    boolean isDefault = Optional.ofNullable(addressBook.getIsDefault())
            .map(StatusConstant.ENABLE::equals)
            .orElse(false);
  
    addressBook.setIsDefault(0);
    
    // 如果当前新增地址设置为默认地址，则将当前用户的所有地址修改为非默认地址
    if(isDefault) {
      addressBookMapper.updateIsDefaultByUserId(addressBook);
      // 设置当前地址为默认地址
      addressBook.setIsDefault(1);
    }
    addressBookMapper.insert(addressBook);
  }
  
  @Override
  public AddressBook getById(Long id) {
    return addressBookMapper.getById(id);
  }
  
  @Override
  public void update(AddressBook addressBook) {
    addressBookMapper.update(addressBook);
  }
  
  @Transactional
  @Override
  public void setDefault(AddressBook addressBook) {
    /* 1、将当前用户的所有地址修改为非默认地址 */
    addressBook.setIsDefault(0);
    addressBook.setUserId(BaseContext.getUserId());
    addressBookMapper.updateIsDefaultByUserId(addressBook);
  
    /* 2、将当前地址改为默认地址 */
    addressBook.setIsDefault(1);
    addressBookMapper.update(addressBook);
  }
  
  @Override
  public void deleteById(Long id) {
    addressBookMapper.deleteById(id);
  }
}
