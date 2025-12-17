package com.holly.exception;

import com.holly.exception.BaseException;
import lombok.NoArgsConstructor;

/**
 * @description
 */
@NoArgsConstructor
public class AccountNotFoundException extends BaseException {
  
  public AccountNotFoundException(String msg) {
    super(msg);
  }
}
