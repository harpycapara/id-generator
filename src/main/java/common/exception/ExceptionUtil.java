package common.exception;

import bank_binding.common.constant.Constant;
import bank_binding.common.enums.ReasonEnum;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.Any;
import com.google.rpc.ErrorInfo;
import com.google.rpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class ExceptionUtil {

  public static String getExInfo(Throwable ex) {
    return String.format("{Exception:%s -- StackTrace:%s}",
                         ex.toString(),
                         org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(ex));
  }

  public static StatusRuntimeException toStatusRuntimeException(ServiceException serviceException) {
    ReasonEnum errorCode = serviceException.getReason();
    String message = serviceException.getMessage();
    String reason = serviceException.getReasonMessage();
    String domain = serviceException.getDomain();
    if (StringUtil.isEmpty(domain) || StringUtil.isEmpty(reason)) {
      domain = Constant.BANK_DOMAIN;
      reason = String.valueOf(errorCode.getValue());
    }
    Map<String, String> metadata = Optional.ofNullable(serviceException.getMetadata()).orElse(ImmutableMap.of());

    ErrorInfo errorDetail =
        ErrorInfo.newBuilder()
            .setReason(reason)
            .setDomain(domain)
            .putAllMetadata(metadata)
            .build();

    Status status = Status.newBuilder()
        .setCode(errorCode.getGrpcStatus())
        .setMessage(Optional.ofNullable(message).orElse(""))
        .addDetails(Any.pack(errorDetail))
        .build();

    return StatusProto.toStatusRuntimeException(status);
  }

}
