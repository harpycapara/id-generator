package common.interceptor.grpc;

import bank_binding.common.exception.ServiceException;
import bank_binding.common.grpc.GrpcErrorDelegate;
import io.grpc.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.core.annotation.Order;

/**
 * @author tuannlh
 */
@Slf4j
@GRpcGlobalInterceptor
@RequiredArgsConstructor
@Order(2)
public class GrpcExceptionInterceptor implements ServerInterceptor {
  private final GrpcErrorDelegate grpcErrorDelegate;

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call,
      Metadata metadata,
      ServerCallHandler<ReqT, RespT> next) {
    ServerCall.Listener<ReqT> delegate;
    try {
      delegate = next.startCall(call, metadata);
    } catch (Exception ex) {
      handleError(call, ex);
      return new ServerCall.Listener<ReqT>() {
      };
    }
    return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(delegate) {
      @Override
      public void onMessage(ReqT message) {
        try {
          super.onMessage(message);
        } catch (Exception ex) {
          handleError(call, ex);
        }
      }

      @Override
      public void onCancel() {
        try {
          super.onCancel();
        } catch (Exception ex) {
          handleError(call, ex);
        }
      }

      @Override
      public void onHalfClose() {
        try {
          super.onHalfClose();
        } catch (Exception ex) {
          handleError(call, ex);
        }
      }
    };
  }

  private <ReqT, RespT> void handleError(ServerCall<ReqT, RespT> call, Exception exception) {
    try {
      if (exception instanceof ServiceException) {
        grpcErrorDelegate.onHalfClose(call, (ServiceException) exception);
      } else {
        grpcErrorDelegate.onHalfClose(call, new ServiceException(exception));
      }
      log.error("Error on handle request: ", exception);
    } catch (NullPointerException ex) {
      log.warn("Failed to handle error, origin error:", exception);
      call.close(Status.INTERNAL.withCause(exception).withDescription("Failed to handle error"), new Metadata());
    }
  }
}
