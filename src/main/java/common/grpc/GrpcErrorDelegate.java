package common.grpc;

import common.exception.ServiceException;
import io.grpc.ServerCall;

public interface GrpcErrorDelegate {
  <ReqT, RespT> void onHalfClose(ServerCall<ReqT, RespT> serverCall, ServiceException exception);
}
