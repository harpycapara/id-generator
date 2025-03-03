package common.http;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * POJO mapping of gRPC ErrorInfo
 * @author tuannlh
 */
@Data
public class ErrorInfo {

  /**
   * gRPC code or HTTP Code
   */
  private int code;

  /**
   * System message
   */
  private String message;

  /**
   * Details for error using for business
   */
  private List<Details> details;

  @Data
  public static class Details {

    /**
     * Reason code of error
     */
    private String reason;

    /**
     * Domain or service that raise this error
     */
    private String domain;

    /**
     * Additional metadata
     */
    private Map<String, String> metadata;
  }
}
