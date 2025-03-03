package common.utils;

public final class MonitorUtils {
  public static final String CACHE_METRIC = "cache_server_handling";
  public static final String DATABASE_METRIC  = "database_server_handling";

  public static final String GRPC_SERVER_METRIC = "grpc_server_handling";
  public static final String GRPC_CLIENT_METRIC = "grpc_client_handling";
  public static final String HTTP_SERVER_METRIC = "http_server_handling";
  public static final String HTTP_CLIENT_METRIC = "http_client_handling";

  public static final String KAFKA_PRODUCER_METRIC = "kafka_producer_handling";
  public static final String KAFKA_WAIT_QUEUE_METRIC = "kafka_wait_queue";
  public static final String KAFKA_CONSUMER_METRIC = "kafka_consumer_handling";
  public static final String KAFKA_MESSAGE_SOURCE_KEY = "message-source";

  public static final String RABBITMQ_PRODUCER_METRIC = "rabbitmq_producer_handling";
  public static final String RABBITMQ_WAIT_QUEUE_METRIC = "rabbitmq_wait_queue";
  public static final String RABBITMQ_CONSUMER_METRIC = "rabbitmq_consumer_handling";
  public static final String RABBITMQ_ENQUEUE_TIME_KEY = "enqueue-time";

  public static final String MESSAGING_HANDLER_NAME_KEY = "message-handler";
  public static final String BINDING_LOG_METRIC = "binding_log";
  public static final String BINDING_LOG_METRIC_V2 = "binding_log_v2";
  public static final String CACHE_TOTAL_GET_BINDING_BANK = "cache_total_get_binding_bank";
  public static final String CACHE_HIT_GET_BINDING_BANK = "cache_hit_get_binding_bank";

  public static final String COUNTER_ERROR_GET_BINDING_BANK = "counter_error_get_binding_bank";

  private MonitorUtils() {
  }
}
