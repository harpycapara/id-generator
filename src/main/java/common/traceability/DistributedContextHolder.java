package common.traceability;

import io.opentracing.References;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMapAdapter;
import io.opentracing.tag.Tags;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DistributedContextHolder implements Serializable {

  private final Map<String, String> contextMap = new HashMap<>();
  private String name;

  public Tracer.SpanBuilder newChild(Tracer tracer, String operationName) {
    SpanContext parentContext = tracer.extract(Format.Builtin.TEXT_MAP, new TextMapAdapter(contextMap));

    Tracer.SpanBuilder spanBuilder = tracer.buildSpan(operationName);

    if (parentContext != null) {
      spanBuilder.asChildOf(parentContext);
    }

    return spanBuilder;
  }

  public Span newChildWithActive(Tracer tracer, String operationName) {
    SpanContext parentContext = tracer.extract(Format.Builtin.TEXT_MAP, new TextMapAdapter(contextMap));

    Tracer.SpanBuilder spanBuilder = tracer.buildSpan(operationName);

    spanBuilder.asChildOf(tracer.activeSpan());
    if (parentContext != null) {
      spanBuilder.asChildOf(parentContext);
    }

    return spanBuilder.start();
  }

  public static DistributedContextHolder fromCurrent(Tracer tracer) {
    DistributedContextHolder holder = new DistributedContextHolder();
    if (tracer == null) {
      return holder;
    }
    Span span = tracer.activeSpan();
    if (span != null) {
      tracer.inject(span.context(), Format.Builtin.TEXT_MAP, new TextMapAdapter(holder.getContextMap()));
    }
    return holder;
  }

  public static Span getFromSpan(Tracer tracer, DistributedContextHolder holder) {
    Tracer.SpanBuilder spanBuilder = tracer
        .buildSpan("from_" + holder.getName())
        .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_PRODUCER);

    spanBuilder.asChildOf(tracer.activeSpan());

    Span span = spanBuilder.start();
    tracer.inject(span.context(), Format.Builtin.TEXT_MAP, new TextMapAdapter(holder.getContextMap()));
    return span;
  }

  public static Span getToSpan(Tracer tracer, DistributedContextHolder holder) {
    SpanContext parentContext = tracer.extract(Format.Builtin.TEXT_MAP, new TextMapAdapter(holder.getContextMap()));
    Tracer.SpanBuilder spanBuilder = tracer
        .buildSpan("to_" + holder.getName())
        .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_CONSUMER);

    if (parentContext != null) {
      spanBuilder.addReference(References.FOLLOWS_FROM, parentContext);
    }

    Span span = spanBuilder.start();
    return span;
  }

}
