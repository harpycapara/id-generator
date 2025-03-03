package common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author trivm2
 */
@Getter
@AllArgsConstructor
public enum ZECode {
  Exception(-1, "Exception"),
  INVALID_PARAMS(-2, "Invalid params"),
  NOT_FOUND(-4, "Not found")
  ;
  private int value;
  private String name;
}
