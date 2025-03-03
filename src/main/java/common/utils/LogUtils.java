package common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
public class LogUtils {

  public static void error(String s, Throwable throwable) {
    String stacktrace = ExceptionUtils.getStackTrace(throwable);
    s += " with error {}, stack trace {}";
    log.error(s, throwable.getMessage(), stacktrace.replace("\n", " "));
  }

  public static void error(String var1, Object... var2) {
    StringBuilder var1Builder = new StringBuilder(var1);
    for (int i = 0; i < var2.length; i++) {
      if (var2[i] instanceof Throwable) {
        Throwable ex = (Throwable) var2[i];
        var2[i] = ex.getMessage() + " " + ExceptionUtils.getStackTrace((Throwable) var2[i]).replace("\n", " ");
        var1Builder.append(" with error {}");
      }
    }
    var1 = var1Builder.toString();

    log.error(var1, var2);
  }
}

