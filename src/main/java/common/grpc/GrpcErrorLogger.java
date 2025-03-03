package common.grpc;

import bank_binding.common.exception.ServiceException;
import io.grpc.ServerCall;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by IntelliJ IDEA.
 * User: chucvv
 * Date: 15/08/2021
 */
@Slf4j
@RequiredArgsConstructor
public class GrpcErrorLogger implements GrpcErrorDelegate {
  private final GrpcErrorDelegate grpcErrorDelegate;

  @Override
  public <ReqT, RespT> void onHalfClose(ServerCall<ReqT, RespT> serverCall, ServiceException exception) {
    log.info("onHalfClose: exception={}", exception.toString());
    grpcErrorDelegate.onHalfClose(serverCall, exception);
  }
}
