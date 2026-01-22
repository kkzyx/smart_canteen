package com.holly.config;

import com.holly.interceptor.JwtTokenClientInterceptor;
import com.holly.interceptor.JwtTokenStoreInterceptor;
import com.holly.json.JacksonObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;

import java.util.List;

/**
 * @description spring mvc配置类
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

  private final JwtTokenStoreInterceptor jwtTokenStoreInterceptor;
  private final JwtTokenClientInterceptor jwtTokenClientInterceptor;

  /**
   * 注册自定义拦截器
   * @param registry 注册器
   */
  @Override
  protected void addInterceptors(InterceptorRegistry registry) {
    log.info("开始注册自定义拦截器...");
    // 管理端拦截器
    registry.addInterceptor(jwtTokenStoreInterceptor)
            .addPathPatterns("/store/**") // 拦截admin下的所有接口
            .excludePathPatterns("/store/employee/login") // 排除登录接口
            .excludePathPatterns("/store/customer-service/**") // 排除客服接口（临时）
            .excludePathPatterns("/client/shopInfo")
    ;

    // 微信小程序用户端拦截器
    registry.addInterceptor(jwtTokenClientInterceptor)
            .addPathPatterns("/client/**")
            .excludePathPatterns("/client/user/login") // 排除登录接口
            .excludePathPatterns("/client/category/**", "/client/dish/**") // 即使不登录也可以正常浏览菜品列表
            .excludePathPatterns("/client/shop/status", "/client/shopInfo");// 排除获取商铺状态接口
  }

  @Bean
  public GroupedOpenApi storeApi() {
    log.info("开始生成【后台】项目接口文档...");
    return GroupedOpenApi.builder()
            .group("管理端接口")
            .packagesToScan("com.holly.controller.store")
            .build();
  }

  @Bean
  public GroupedOpenApi clientApi() {
    log.info("开始生成【用户端】项目接口文档...");
    return GroupedOpenApi.builder()
            .group("用户端接口")
            .packagesToScan("com.holly.controller.client")
            .build();
  }

  @Override
  protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    log.info("开始扩展消息转换器...");
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(new JacksonObjectMapper());
    converters.add(6, converter);
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
  }

  @Override
  protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    log.info("开始注册静态资源映射...");

    registry.addResourceHandler("/doc.html")
            .addResourceLocations("classpath:/META-INF/resources/");
    registry.addResourceHandler("/favicon.ico")
            .addResourceLocations("classpath:/META-INF/resources/");
    registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
    registry.addResourceHandler("/v3/**")
            .addResourceLocations("classpath:/META-INF/resources/v3/");
  }
}
