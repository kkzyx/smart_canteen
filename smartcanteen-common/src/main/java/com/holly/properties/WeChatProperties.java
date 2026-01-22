package com.holly.properties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description
 */
@Data
@Component
@Schema(description = "微信小程序属性配置类")
@ConfigurationProperties(prefix = "training.wechat")
public class WeChatProperties {

  @Schema(description = "小程序的appid")
  private String appid;

  @Schema(description = "小程序的秘钥")
  private String secret;
}

