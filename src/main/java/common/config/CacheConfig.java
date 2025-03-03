package common.config;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "cache")
@AllArgsConstructor
@NoArgsConstructor
public class CacheConfig {
  private int timeToLive;
  private TimeUnit timeUnit;
  private Duration oaoTransTtl;
}