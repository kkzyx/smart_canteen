package com.holly.exception;


import com.holly.exception.BaseException;
import lombok.NoArgsConstructor;

/**
 * @description 套餐启用失败异常
 */
@NoArgsConstructor
public class SetmealEnableFailedException extends BaseException {
  
  public SetmealEnableFailedException(String msg) {
    super(msg);
  }
}
