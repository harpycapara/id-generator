package common.traceability;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.*;

public class MDCThreadPoolExecutor extends ThreadPoolExecutor {

  public static MDCThreadPoolExecutor newFixedThreadPool(int nThreads,
                                                         int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                                         BlockingQueue<Runnable> workQueue) {
    return new MDCThreadPoolExecutor(nThreads, maximumPoolSize, keepAliveTime, unit, workQueue);
  }

  public static MDCThreadPoolExecutor newFixedThreadPool(int nThreads) {
    return newFixedThreadPool(nThreads, nThreads,
                              0L, TimeUnit.MILLISECONDS,
                              new LinkedBlockingQueue<>());
  }

  public static MDCThreadPoolExecutor newCachedThreadPool() {
    return new MDCThreadPoolExecutor(0, Integer.MAX_VALUE,
                                     60L, TimeUnit.SECONDS,
                                     new SynchronousQueue<>());
  }

  MDCThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                                long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
  }

  @Override
  public void execute(Runnable runnable) {
    final Map<String, String> context = MDC.getCopyOfContextMap();
    if (context == null) {
      super.execute(runnable);
      return;
    }
    super.execute(wrap(runnable, context));
  }

  public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
    return () -> {
      MDC.setContextMap(context);
      try {
        runnable.run();
      } finally {
        MDC.clear();
      }
    };
  }

}
