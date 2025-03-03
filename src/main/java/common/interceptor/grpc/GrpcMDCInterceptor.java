package common.interceptor.grpc;

import io.grpc.*;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;

@GRpcGlobalInterceptor
@Order(1)
public class GrpcMDCInterceptor implements ServerInterceptor {

  public static final Context.Key<String> API_NAME_CONTEXT_KEY = Context.key("apiname");

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call,
      Metadata metadata,
      ServerCallHandler<ReqT, RespT> next) {
    ServerCall.Listener<ReqT> delegate;
    try {
      String fullMethodName = call.getMethodDescriptor().getFullMethodName();
      String apiName = fullMethodName.substring(fullMethodName.lastIndexOf("/") + 1);
      MDC.put("apiname", apiName);
      Context context = Context.current().withValue(API_NAME_CONTEXT_KEY, apiName);
      delegate = Contexts.interceptCall(context, call, metadata, next);
    } catch (Exception ex) {
      MDC.remove("apiname");
      throw ex;
    }
    return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(delegate) {
      @Override
      public void onComplete() {
        MDC.remove("apiname");
        super.onComplete();
      }
    };
  }

}
