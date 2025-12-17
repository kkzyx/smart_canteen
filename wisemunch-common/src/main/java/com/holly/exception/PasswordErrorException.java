package com.holly.exception;

import com.holly.exception.BaseException;
import lombok.NoArgsConstructor;

/**
 * @description 密码错误异常
 */
@NoArgsConstructor
public class PasswordErrorException extends BaseException {
  
  public PasswordErrorException(String msg) {
    super(msg);
  }
}
