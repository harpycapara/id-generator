package common.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@Data
@ConfigurationProperties(prefix = "cache-redisson")
public class CacheRedissonConfig {
  private CacheConfig bindingBank;
  private CacheConfig registerBypassOtp;
  private CacheConfig upgradeBindingBank;

  @Data
  public static class CacheConfig {
    private int timeToLive;
    private TimeUnit timeUnit;
  }
}