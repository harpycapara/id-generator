package common.interceptor;

import bank_binding.common.utils.ProtobufUtils;
import bank_binding.common.utils.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.protobuf.MessageOrBuilder;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Set;

/**
 * @author tuannlh
 */
@Slf4j
public class ClientInterceptorImpl implements ClientInterceptor {

  private final String clientId;
  private final String clientKey;
  private final String serviceName;

  /**
   * key format: <api name>.<request/response>
   */
  private final Set<String> ignoreLogMap;
  private final boolean isEnableLog;

  public ClientInterceptorImpl(String clientId, String clientKey, String serviceName, boolean isEnableLog,
                               Iterable<String> ignoreLogMap) {
    this.clientId = clientId;
    this.clientKey = clientKey;
    this.serviceName = serviceName;
    this.isEnableLog = isEnableLog;
    this.ignoreLogMap = Optional.ofNullable(ignoreLogMap).map(ImmutableSet::copyOf).orElse(ImmutableSet.of());
  }

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
      MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
    return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
        channel.newCall(methodDescriptor, callOptions)) {

      @Override
      public void sendMessage(ReqT message) {
        if (isEnableLog) {
          log.info("service={}, method={}, request={}",
                   serviceName, methodDescriptor.getFullMethodName(), ProtobufUtils.print((MessageOrBuilder) message));
        }
        super.sendMessage(message);
      }

      @Override
      public void start(Listener<RespT> responseListener, Metadata headers) {
        if (!Strings.isEmpty(clientId)) {
          headers.put(Metadata.Key.of("client-id", Metadata.ASCII_STRING_MARSHALLER), clientId);
        }
        if (!Strings.isEmpty(clientKey)) {
          headers.put(Metadata.Key.of("client-key", Metadata.ASCII_STRING_MARSHALLER), clientKey);
        }

        super.start(
            new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(
                responseListener) {
              @Override
              public void onHeaders(Metadata headers) {
                super.onHeaders(headers);
              }

              @Override
              public void onMessage(RespT message) {
                if (isEnableLog) {
                  String methodName = methodDescriptor.getFullMethodName();
                  String ignoreLogKey = methodName.substring(methodName.lastIndexOf("/") + 1) + ".response";
                  if (ignoreLogMap.contains(ignoreLogKey)) {
                    log.info("service={}, method={}, response={}",
                             serviceName, methodName, "ignored*");
                  } else {
                    log.info("service={}, method={}, response={}",
                             serviceName, methodName, ProtobufUtils.print((MessageOrBuilder) message));
                  }
                }
                super.onMessage(message);
              }
            },
            headers);
      }
    };
  }

}
