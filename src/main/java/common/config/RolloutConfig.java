package common.config;

import infrastructure.cache.CacheClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Getter
@Setter
@RequiredArgsConstructor
public class RolloutConfig {
  private final CacheClient cacheClient;
  private final int DEFAULT_NOT_ROLLOUT = -1;
  private final String FROM = "from";
  private final String TO = "to";

  public int getFrom(String key) {
    Map<String, Integer> config = cacheClient.getMapContent(key);
    if (config != null && !config.isEmpty()) {
      return config.getOrDefault(FROM, DEFAULT_NOT_ROLLOUT);
    }
    return DEFAULT_NOT_ROLLOUT;
  }

  public int getTo(String key) {
    Map<String, Integer> config = cacheClient.getMapContent(key);
    if (config != null) {
      return config.getOrDefault(TO, DEFAULT_NOT_ROLLOUT);
    }
    return DEFAULT_NOT_ROLLOUT;
  }

  public void setFromToWithKey(String key, int from, int to) {
    Map<String, Integer> config = new HashMap<>();
    config.put(FROM,from);
    config.put(TO,to);
    cacheClient.setMapContent(key, config);
  }

  public boolean isRolloutNewFeature(String key, int lastTwoNumber) {
    int from = getFrom(key);
    int to = getTo(key);

    if (lastTwoNumber >= from && lastTwoNumber <= to) {
      return true;
    }
    return false;
  }
}
