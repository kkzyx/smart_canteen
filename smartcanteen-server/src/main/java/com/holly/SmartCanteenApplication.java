package com.holly;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @description 主程序启动类
 */
@Slf4j
@SpringBootApplication(exclude = {
    dev.langchain4j.community.store.embedding.redis.spring.RedisEmbeddingStoreAutoConfiguration.class
})
@EnableAsync
@MapperScan("com.holly.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class SmartCanteenApplication {
  public static void main(String[] args) throws UnknownHostException {
    SpringApplication app = new SpringApplicationBuilder(SmartCanteenApplication.class).build(args);;
    Environment env = app.run(args).getEnvironment();
    String protocol = "http";
    if (env.getProperty("server.ssl.key-store") != null) {
      protocol = "https";
    }
    log.info("--/\n---------------------------------------------------------------------------------------\n\t" +
                    "Application '{}' is running! Access URLs:\n\t" +
                    "Local: \t\t{}://localhost:{}\n\t" +
                    "External: \t{}://{}:{}\n\t" +
                    "Profile(s): \t{}" +
                    "\n---------------------------------------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            env.getProperty("server.port"),
            protocol,
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port"),
            env.getActiveProfiles());
  }
}
