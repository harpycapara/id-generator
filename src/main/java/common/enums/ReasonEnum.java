package common.enums;

import com.google.rpc.Code;
import io.grpc.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum ReasonEnum {
  MAX_PER_DAY_UNSATISFIED(3, "Unsatisfied maximum amount per day"),
  MAX_PER_TRANS_UNSATISFIED(2, "Unsatisfied maximum amount per transaction"),
  MIN_PER_TRANS_UNSATISFIED(1, "Unsatisfied minimum limitation per transaction"),
  UNDEFINED(0, "Undefined"),
  EXCEPTION(-1, "Exception"),
  INVALID_PARAMS(-2, Code.INVALID_ARGUMENT.getNumber(), "Params is invalid"),
  AUTHENTICATION_FAILED(-3, Code.UNAUTHENTICATED.getNumber(), "Authentication failed"),
  PERMISSION_DENIED(-4, Code.PERMISSION_DENIED.getNumber(), "Permission denied"),
  OBS_EXCEPTION(-5, "Call api get-list-card-and-account failed"),
  CHECK_BINDING_EXIST_EXCEPTION(-6, "Call api check-binding-exist failed"),
  UN_SUPPORT(-7, "unsupport feature"),
  USER_IS_NOT_TRANSACTION_OWNER(-8, "User is not transaction owner"),
  CANNOT_FIND_VALIDATION(-9, "cannot find validation"),
  GET_LIST_CARD_AND_ACCOUNT_EXCEPTION(-10, "Call api get-list-card-and-account failed"),
  SAVE_CACHE_FAIL(-11, "Save cache fail"),
  SAVE_DATABASE_FAIL(-12, "Save database fail"),
  REMOVE_DATABASE_FAIL(-13, "Remove in database fail"),
  REMOVE_CACHE_FAIL(-14, "Remove in cache fail"),
  BANK_CODE_NOT_FOUND(-15, "Bank code not found"),
  BANK_HOLDER_NAME_NOT_FOUND(-16, Status.NOT_FOUND.getCode().value(), "Bank holder name not found"),
  ;

  private static final Map<Integer, ReasonEnum> MAP = Arrays.stream(ReasonEnum.values())
      .collect(Collectors.toMap(ReasonEnum::getValue, Function.identity()));

  private final int value;
  private final int grpcStatus;
  private final String message;

  ReasonEnum(int value, String message) {
    this(value, value, message);
  }
}
