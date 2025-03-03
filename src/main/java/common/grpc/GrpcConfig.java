package common.grpc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {
  @Bean
  public GrpcErrorDelegate grpcErrorDelegate() {
    return new GrpcErrorLogger(new GrpcErrorHandler());
  }
}
