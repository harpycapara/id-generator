package common.interceptor.grpc;

import bank_binding.common.enums.MonitorEnum;
import bank_binding.common.traceability.MonitorFactory;
import io.grpc.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.core.annotation.Order;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
@GRpcGlobalInterceptor
@Order(3)
public class GrpcMonitorInterceptor implements ServerInterceptor {

  private final MonitorFactory monitorFactory;

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call,
      Metadata metadata,
      ServerCallHandler<ReqT, RespT> next) {
    ServerCall.Listener<ReqT> delegate;
    try {
      delegate = next.startCall(call, metadata);
    } catch (Exception ex) {
      monitorFactory.getCounterOfApi(call.getMethodDescriptor().getFullMethodName(), MonitorEnum.GRPC_REQUEST_FAILED).increment();
      throw ex;
    }
    return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(delegate) {

      private final AtomicBoolean errorCheckPoint = new AtomicBoolean(false);

      @Override
      public void onMessage(ReqT message) {
        try {
          super.onMessage(message);
        } catch (Exception ex) {
          errorCheckPoint.set(true);
          throw ex;
        }
      }

      @Override
      public void onCancel() {
        try {
          super.onCancel();
        } catch (Exception ex) {
          errorCheckPoint.set(true);
          throw ex;
        }
      }

      @Override
      public void onHalfClose() {
        try {
          super.onHalfClose();
        } catch (Exception ex) {
          errorCheckPoint.set(true);
          throw ex;
        }
      }

      @Override
      public void onComplete() {
        try {
          super.onComplete();
        } catch (Exception ex) {
          errorCheckPoint.set(true);
          throw ex;
        } finally {
          MonitorEnum monitorEnum = errorCheckPoint.get() ? MonitorEnum.GRPC_REQUEST_FAILED :
              MonitorEnum.GRPC_REQUEST_SUCCESS;
          monitorFactory.getCounterOfApi(call.getMethodDescriptor().getFullMethodName(), monitorEnum).increment();
        }
      }
    };
  }

}
