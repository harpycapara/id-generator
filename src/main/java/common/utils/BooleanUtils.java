package common.utils;

import org.jetbrains.annotations.Nullable;

public class BooleanUtils {

  public static @Nullable Boolean exactParse(@Nullable String value) {
    if (value == null) {
      return null;
    }
    return "true".equals(value) ? Boolean.TRUE : "false".equals(value) ? Boolean.FALSE : null;
  }

}
