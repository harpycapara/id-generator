package common.enums;

public enum MonitorEnum {

  GRPC_REQUEST_SUCCESS,
  GRPC_REQUEST_FAILED;

  public static MonitorEnum fromThrowable(Throwable throwable) {
    return GRPC_REQUEST_FAILED;
  }

}
