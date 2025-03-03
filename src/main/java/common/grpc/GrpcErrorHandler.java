package common.grpc;

import bank_binding.common.exception.ExceptionUtil;
import bank_binding.common.exception.ServiceException;
import io.grpc.ServerCall;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class GrpcErrorHandler implements GrpcErrorDelegate {

  @Override
  public <ReqT, RespT> void onHalfClose(ServerCall<ReqT, RespT> serverCall, ServiceException exception) {
    val statusEx = ExceptionUtil.toStatusRuntimeException(exception);
    serverCall.close(Status.fromThrowable(statusEx), Status.trailersFromThrowable(statusEx));
  }

}
