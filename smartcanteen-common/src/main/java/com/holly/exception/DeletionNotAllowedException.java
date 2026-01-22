package com.holly.exception;

import com.holly.exception.BaseException;
import lombok.NoArgsConstructor;

/**
 * @description
 */
@NoArgsConstructor
public class DeletionNotAllowedException extends BaseException {
  
  public DeletionNotAllowedException(String msg) {
    super(msg);
  }
}
