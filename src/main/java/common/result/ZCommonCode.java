package common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author trivm2
 */
@Getter
@AllArgsConstructor
public enum ZCommonCode {
  Exception(-1000001, "Exception"),
  INVALID_PARAMS(-1000002, "Invalid params"),
  NOT_FOUND(-1000003, "Not found")
  ;
  private int value;
  private String name;
}
