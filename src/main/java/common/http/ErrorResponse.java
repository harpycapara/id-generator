package common.http;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author tuannlh
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
  private ErrorInfo errorInfo;
}
