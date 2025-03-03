package common.traceability;

public interface Traceability {
  String getTraceId();

  DistributedContextHolder getDistributedContextHolder();

}
