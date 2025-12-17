package com.holly.handler;

import com.holly.constant.MessageConstant;
import com.holly.exception.BaseException;
import com.holly.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @description 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  
  /**
   * 处理业务异常
   *
   * @param e 业务异常
   * @return JSON 格式的错误信息
   */
  @ExceptionHandler
  public Result<?> exceptionHandler(BaseException e) {
    log.error("捕获【BaseException】的异常信息==> {}", e.getMessage());
    int code = e.getCode();
    if (code != 0) {
      return Result.error(code, e.getMessage());
    }
    return Result.error(e.getMessage());
  }

  /**
   * 处理其他异常
   * @param e
   * @return
   */
  @ExceptionHandler(Exception.class)
  public Result<?> exceptionHandler(Exception e) {
    e.printStackTrace();
    log.error("捕获【Exception】的异常信息==> {}", e.getMessage());
    return Result.error(e.getMessage());
  }
  
  /**
   * 处理 SQL 异常
   * @param e SQL 异常
   * @return JSON 格式的错误信息
   */
  @ExceptionHandler
  public Result<?> exceptionHandler(SQLIntegrityConstraintViolationException e) {
    // 报错信息示例：Duplicate entry 'zhangsan' for key 'employee.idx_username'
    log.error("捕获【SQLIntegrityConstraintViolationException】的异常信息==> {}", e.getMessage());
    String message = e.getMessage();
    if (message.contains("Duplicate entry")) {
      // 分割字符获取用户名
      String[] split = message.split(" ");
      String username = "【" + split[2].replaceAll("'", "") + "】";
      // 用户名已存在
      String msg = username + MessageConstant.ALREADY_EXISTS;
      return Result.error(msg);
    }
    return Result.error(MessageConstant.UNKNOWN_ERROR);
  }
}
