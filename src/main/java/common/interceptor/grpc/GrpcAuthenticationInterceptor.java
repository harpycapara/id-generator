package common.interceptor.grpc;

import bank_binding.common.interceptor.AuthenticateService;
import io.grpc.*;
import io.opentracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

@GRpcGlobalInterceptor
@RequiredArgsConstructor
@Slf4j
@Order(5)
public class GrpcAuthenticationInterceptor implements ServerInterceptor {
  public static final Context.Key<String> CLIENT_ID_CONTEXT_KEY = Context.key("client-id");

  private static final Metadata.Key<String> CLIENT_ID_METADATA_KEY =
      Metadata.Key.of("client-id", Metadata.ASCII_STRING_MARSHALLER);

  private static final Metadata.Key<String> CLIENT_KEY_METADATA_KEY =
      Metadata.Key.of("client-key", Metadata.ASCII_STRING_MARSHALLER);

  private static final Metadata.Key<String> B3_METADATA_KEY =
      Metadata.Key.of("x-b3-traceid", Metadata.ASCII_STRING_MARSHALLER);

  private final AuthenticateService authenService;
  @Autowired
  private Tracer tracer;

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> serverCall,
      Metadata metadata,
      ServerCallHandler<ReqT, RespT> serverCallHandler) {
    String fullMethodName = serverCall.getMethodDescriptor().getFullMethodName();

    setB3Propagation(metadata.get(B3_METADATA_KEY));

    String clientId = metadata.get(CLIENT_ID_METADATA_KEY);
    String clientKey = metadata.get(CLIENT_KEY_METADATA_KEY);

    log.info("Client ID: {}", clientId);

    authenService.authenticate(clientId, clientKey, fullMethodName);
    Context context = Context.current().withValue(CLIENT_ID_CONTEXT_KEY, clientId);
    return Contexts.interceptCall(context, serverCall, metadata, serverCallHandler);
  }

  private void setB3Propagation(String b3Header) {
    if (StringUtils.isEmpty(b3Header)) {
      MDC.put("X-B3-TraceId", tracer.scopeManager().activeSpan().context().toTraceId());
    } else {
      MDC.put("X-B3-TraceId", b3Header);
    }
  }

}
