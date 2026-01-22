package com.holly.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * AI推荐请求DTO
 */
@Data
@Schema(description = "AI推荐请求数据传输对象")
public class RecommendRequestDTO implements Serializable {

    @Schema(description = "用户偏好描述")
    private String preference;

    @Schema(description = "店铺ID")
    private Long shopId;
}

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//import java.io.Serializable;
//
///**
// * AI推荐请求DTO
// */
//@Data
//@ApiModel(description = "AI推荐请求数据传输对象")
//public class RecommendRequestDTO implements Serializable {
//
//    @ApiModelProperty("用户偏好描述")
//    private String preference;
//
//    @ApiModelProperty("店铺ID")
//    private Long shopId;
//}
