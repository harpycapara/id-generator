package common.result;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author trivm2
 */
@Getter
@ToString
@Builder
@NoArgsConstructor
public class ZError {
  int code;
  String message;
  Throwable origException;

  public ZError(int code) {
    this(code, null, null);
  }

  public ZError(int code, String message) {
    this(code, message, null);
  }

  public ZError(int code, String message, Throwable origException) {
    this.code = code;
    this.message = message;
    this.origException = origException;
  }
}
