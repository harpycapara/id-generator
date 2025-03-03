package common.config;

import common.traceability.MDCThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;

@Configuration
public class GlobalExecutorServiceConfig {

  /**
   * Global executor service for application
   * Usage: for asynchronous task in flows
   */
  @Bean
  public ExecutorService globalExecutorService() {
    return MDCThreadPoolExecutor.newCachedThreadPool();
  }

}
