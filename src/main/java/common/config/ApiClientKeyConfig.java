package common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "api-client-key")
public class ApiClientKeyConfig {

  public static final String API_CLIENT_BEAN = "API_CLIENT_CONFIG";
  public static final String CLIENT_KEY_BEAN = "CLIENT_KEY_CONFIG";

  private Map<String, List<String>> apiClient;
  private Map<String, String> clientKey;

  @Bean(API_CLIENT_BEAN)
  public Map<String, List<String>> createApiClientBean() {
    return apiClient;
  }

  @Bean(CLIENT_KEY_BEAN)
  public Map<String, String> createClientKeyBean() {
    return clientKey;
  }
}