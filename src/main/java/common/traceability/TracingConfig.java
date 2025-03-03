package common.traceability;

import io.jaegertracing.internal.MDCScopeManager;
import io.opentracing.contrib.java.spring.jaeger.starter.TracerBuilderCustomizer;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("opentracing")
@Data
public class TracingConfig {

  private Interceptor interceptor;

  @Data
  public static class Interceptor {
    private boolean verbose;
  }

  @Bean
  public TracerBuilderCustomizer tracerBuilderCustomizer() {
    return builder -> builder.withScopeManager(new MDCScopeManager.Builder().build());
  }

}
