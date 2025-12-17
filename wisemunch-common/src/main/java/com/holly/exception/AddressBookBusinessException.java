package com.holly.exception;

import com.holly.exception.BaseException;
import lombok.NoArgsConstructor;

/**
 * @description
 */
@NoArgsConstructor
public class AddressBookBusinessException extends BaseException {
  
  public AddressBookBusinessException(String msg) {
    super(msg);
  }
}
