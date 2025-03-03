package common.traceability;

import bank_binding.infrastructure.kafka.KafkaFactory;
import bank_binding.infrastructure.session.DecorateEntitySessionService;
import bank_binding.infrastructure.session.Session;
import bank_binding.infrastructure.session.SessionOption;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.contrib.grpc.TracingClientInterceptor;
import io.opentracing.contrib.grpc.TracingServerInterceptor;
import io.opentracing.contrib.kafka.spring.TracingConsumerFactory;
import io.opentracing.contrib.kafka.spring.TracingProducerFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@RequiredArgsConstructor
public class TracingBean {

  private final TracingConfig config;
  private final Tracer tracer;

  @Bean
  public TracingClientInterceptor tracingClientInterceptor() {
    TracingClientInterceptor.Builder builder = TracingClientInterceptor
        .newBuilder()
        .withTracer(tracer)
        .withStreaming()
        .withTracedAttributes(TracingClientInterceptor.ClientRequestAttribute.ALL_CALL_OPTIONS,
                              TracingClientInterceptor.ClientRequestAttribute.HEADERS);
    if (config.getInterceptor().isVerbose()) {
      builder.withVerbosity();
    }
    return builder.build();
  }

  @Bean
  public TracingServerInterceptor tracingServerInterceptor() {
    TracingServerInterceptor.Builder builder = TracingServerInterceptor
        .newBuilder()
        .withTracer(tracer)
        .withStreaming()
        .withTracedAttributes(TracingServerInterceptor.ServerRequestAttribute.CALL_ATTRIBUTES,
                              TracingServerInterceptor.ServerRequestAttribute.HEADERS);
    if (config.getInterceptor().isVerbose()) {
      builder.withVerbosity();
    }
    return builder.build();
  }

  @Bean
  public KafkaFactory.ConsumerDecorate kafkaConsumerDecorate() {
    return factory -> new TracingConsumerFactory<>(factory, tracer);
  }

  @Bean
  public KafkaFactory.ProducerDecorate kafkaProducerDecorate() {
    return factory -> new TracingProducerFactory<>(factory, tracer);
  }

  @Bean
  @Order(2)
  public DecorateEntitySessionService.AccessSessionDecorator traceabilitySessionDecorator() {
    final class TracingEntityServiceDecorator implements DecorateEntitySessionService.AccessSessionDecorator {

      @Override
      public @Nullable <ID, E> AutoCloseable decorate(ID entityId, SessionOption option, Session<E> session) {
        if (SessionOption.DISTRIBUTED_TRACING.get(option) && session.getEntity() instanceof Traceability && tracer != null) {
          Traceability traceability = (Traceability) session.getEntity();
          DistributedContextHolder distributedContextHolder = traceability.getDistributedContextHolder();
          if (distributedContextHolder == null) {
            return null;
          }
          Span span = distributedContextHolder.newChildWithActive(tracer, MDC.get("apiname"));
          return span::finish;
        }
        return null;
      }

    }
    return new TracingEntityServiceDecorator();
  }

  @Bean
  @Order(3)
  public DecorateEntitySessionService.AccessSessionDecorator traceLogSessionDecorator() {
    final class TraceLogEntityServiceDecorator implements DecorateEntitySessionService.AccessSessionDecorator {

      @Override
      public @Nullable <ID, E> AutoCloseable decorate(ID entityId, SessionOption option, Session<E> session) {
        E entity = session.getEntity();
        String traceId = entity instanceof Traceability ? ((Traceability) entity).getTraceId() : null;
        MDC.put("traceId", traceId);
        return () -> MDC.remove("traceId");
      }

    }
    return new TraceLogEntityServiceDecorator();
  }

}
