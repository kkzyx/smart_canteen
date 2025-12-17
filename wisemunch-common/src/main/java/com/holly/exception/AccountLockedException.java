package com.holly.exception;

import com.holly.exception.BaseException;
import lombok.NoArgsConstructor;

/**
 * @description
 */
@NoArgsConstructor
public class AccountLockedException extends BaseException {
  
  public AccountLockedException(String msg) {
    super(msg);
  }
  
}
