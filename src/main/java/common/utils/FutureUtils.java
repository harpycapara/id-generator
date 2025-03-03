package common.utils;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class FutureUtils {

  public static <T> CompletableFuture<T> supplyAsyncWithMDC(Supplier<T> supplier) {
    final Map<String, String> mdcContext = MDC.getCopyOfContextMap();
    return CompletableFuture.supplyAsync(() -> {
      if (mdcContext != null) {
        MDC.setContextMap(mdcContext);
      }
      try {
        return supplier.get();
      } finally {
        MDC.clear();
      }
    });
  }

}
