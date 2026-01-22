package com.holly.result;


//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

import static com.holly.constant.MessageConstant.OPERATE_FAILED;

/**
 * @description 响应结果
 */
@Data
@Schema(description = "响应结果")
public class Result<T> implements Serializable {
  /** 结果编码：200-成功，0-和其他数字均表示失败 */
  @Schema(description = "响应状态码")
  private Integer code;

  @Schema(description = "结果信息")
  private String msg;

  @Schema(description = "返回的数据")
  private T data;
  
  public static <T> Result<T> error(String msg) {
    Result<T> result = new Result<>();
    result.setMsg(msg);
    result.setCode(0);
    return result;
  }
  
  public static <T> Result<T> error(int code, String msg) {
    Result<T> result = new Result<>();
    result.setMsg(msg);
    result.setCode(code);
    return result;
  }

  public static <T> Result<T> error() {
    Result<T> result = new Result<>();
    result.setMsg(OPERATE_FAILED);
    result.setCode(0);
    return result;
  }
  
  public static <T> Result<T> success() {
    Result<T> result = new Result<>();
    result.setCode(200);
    result.setMsg("操作成功");
    return result;
  }
  
  public static <T> Result<T> success(T object) {
    Result<T> result = new Result<>();
    result.setData(object);
    result.setCode(200);
    return result;
  }
  
  public static <T> Result<T> success(T object, String msg) {
    Result<T> result = new Result<>();
    result.setData(object);
    result.setMsg(msg);
    result.setCode(200);
    return result;
  }
}
