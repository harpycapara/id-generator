package common.exception;

import bank_binding.common.enums.ReasonEnum;
import com.google.protobuf.Any;
import com.google.rpc.Code;
import com.google.rpc.ErrorInfo;
import com.google.rpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StatusRuntimeExceptionBuilder {
  private Code code = Code.UNKNOWN;
  private String message = "Unknown";
  private String reason = ReasonEnum.UNDEFINED.toString();
  private final String domain = "common";

  public static StatusRuntimeExceptionBuilder newFailedPreConditionBuilder() {
    StatusRuntimeExceptionBuilder builder = new StatusRuntimeExceptionBuilder();
    builder.code = Code.FAILED_PRECONDITION;
    return builder;
  }

  public StatusRuntimeException build() {
    ErrorInfo errorInfo = ErrorInfo.newBuilder().setReason(reason).setDomain(domain).build();

    return StatusProto.toStatusRuntimeException(
        Status.newBuilder()
            .setCode(code.getNumber())
            .setMessage(message)
            .addDetails(Any.pack(errorInfo))
            .build());
  }

  public StatusRuntimeExceptionBuilder setCode(Code code) {
    this.code = code;
    return this;
  }

  public StatusRuntimeExceptionBuilder setMessage(String message) {
    this.message = message;
    return this;
  }

  public StatusRuntimeExceptionBuilder setReason(String reason) {
    this.reason = reason;
    return this;
  }
}
