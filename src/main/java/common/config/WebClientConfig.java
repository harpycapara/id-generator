package common.config;

import bank_binding.infrastructure.config.ObsConfig;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;
import reactor.netty.tcp.ProxyProvider;
import reactor.netty.tcp.TcpClient;

@Configuration
public class WebClientConfig {

  private final ObsConfig obsConfig;

  @Autowired
  public WebClientConfig(ObsConfig obsConfig) {
    this.obsConfig = obsConfig;
  }

  @Bean
  public WebClient webClient() {
    // This is implicit default if you use TcpClient#create()
    ConnectionProvider connectionProvider = ConnectionProvider.fixed("tcp", 500);
    LoopResources loopResources = LoopResources.create("reactor-tcp");

    TcpClient tcpClient =
        TcpClient.create(connectionProvider)
            .runOn(loopResources)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, obsConfig.getConnectTimeoutMillis())
            .doOnConnected(
                connection ->
                    connection
                        .addHandlerLast(new ReadTimeoutHandler(obsConfig.getReadTimeout(),
                                                               obsConfig.getTimeUnit()))
                        .addHandlerLast(new WriteTimeoutHandler(obsConfig.getWriteTimeout(),
                                                                obsConfig.getTimeUnit())));

    if (obsConfig.isProxyEnabled()) {
      tcpClient =
          tcpClient.proxy(
              typeSpec ->
                  typeSpec.type(ProxyProvider.Proxy.HTTP).address(obsConfig.getAddress()));
    }

    HttpClient httpClient = HttpClient.from(tcpClient);

    ClientHttpConnector clientConnector = new ReactorClientHttpConnector(httpClient);

    return WebClient.builder()
        .baseUrl(obsConfig.getBaseUrl())
        .clientConnector(clientConnector)
        .build();
  }
}
