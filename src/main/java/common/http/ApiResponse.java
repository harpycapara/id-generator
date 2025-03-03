package common.http;

import common.result.ZError;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class ApiResponse<T> {
  private int code;
  private T data;
  private String message;
  private String traceId;

  public static <T> ApiResponse<T> ok() {
    return build(0, null, null, null);
  }

  public static <T> ApiResponse<T> ok(T data) {
    return build(0, data, null, null);
  }

  public static <T> ApiResponse<T> failure(int code) {
    return build(code, null, null, null);
  }

  public static <T> ApiResponse<T> failure(int code, String message, String traceId) {
    return build(code, null, message, traceId);
  }

  public static <T> ApiResponse<T> failure(int code, String message) {
    return build(code, null, message, null);
  }

  public static <T> ApiResponse<T> failure(@NotNull ZError error) {
    return build(error.getCode(), null, error.getMessage(), null);
  }

  public static <T> ApiResponse<T> failure(@NotNull ZError error, String traceId) {
    return build(error.getCode(), null, error.getMessage(), traceId);
  }

  public static <T> ApiResponse<T> build(int code, T data, String message, String traceId) {
    return ApiResponse.<T>builder()
        .code(code)
        .data(data)
        .traceId(traceId)
        .build();
  }
}
