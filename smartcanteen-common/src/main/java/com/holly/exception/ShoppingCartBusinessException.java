package com.holly.exception;

import com.holly.exception.BaseException;
import lombok.NoArgsConstructor;

/**
 * @description
 */
@NoArgsConstructor
public class ShoppingCartBusinessException extends BaseException {
  
  public ShoppingCartBusinessException(String msg) {
    super(msg);
  }
}
