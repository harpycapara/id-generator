package common.grpc;

import common.interceptor.ClientInterceptorImpl;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannelBuilder;
import io.opentracing.contrib.grpc.TracingClientInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GrpcChannelBuilder {

  private final TracingClientInterceptor tracingClientInterceptor;

  public Channel build(String serviceName, Config config) {
    ManagedChannelBuilder<?> managedChannel;
    managedChannel = config.useTarget() ? ManagedChannelBuilder.forTarget(config.getTarget())
        : ManagedChannelBuilder.forAddress(config.getGrpcHost(),
                                           config.getGrpcPort());
    if (!config.isUseSsl()) {
      managedChannel.usePlaintext();
    }

    ClientInterceptor clientInterceptor = new ClientInterceptorImpl(config.getClientId(),
                                                                    config.getClientKey(),
                                                                    serviceName,
                                                                    config.isEnableLog(),
                                                                    config.getExcludeLog());

    Channel channel = managedChannel.build();
    return tracingClientInterceptor.intercept(
        ClientInterceptors.intercept(
            channel,
            clientInterceptor
        )
    );
  }

  public static interface Config {
    String getGrpcHost();

    int getGrpcPort();

    boolean isUseSsl();

    String getClientId();

    String getClientKey();

    default boolean isEnableLog() {
      return true;
    }

    List<String> getExcludeLog();

    default boolean useTarget() {return false;}

    default String getTarget() {return null;}

  }

}
