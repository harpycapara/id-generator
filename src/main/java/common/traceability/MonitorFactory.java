package common.traceability;

import bank_binding.common.enums.MonitorEnum;
import bank_binding.common.utils.MonitorUtils;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MonitorFactory {

  private final MeterRegistry registry;
  private final Map<String, Counter> apiCounterMap = new HashMap<>();

  public <E extends Enum<E>> Counter createEnumCounters(String name, Iterable<Tag> tags, String enumTagKey, E e) {
    return Counter.builder(name).tag(enumTagKey, e.name()).tags(tags).register(registry);
  }

  public Counter getCounterOfApi(String apiName, MonitorEnum monitorEnum) {
    String key = apiName + "-" + monitorEnum.name();
    Counter counter = apiCounterMap.get(key);
    if (counter == null) {
      synchronized (apiCounterMap) {
        counter = apiCounterMap.get(key);
        if (counter == null) {
          counter = createEnumCounters(MonitorUtils.GRPC_SERVER_METRIC + "_status",
                                       Tags.of("api", apiName), "status", monitorEnum);
          apiCounterMap.put(key, counter);
        }
      }
    }
    return counter;
  }

}
