package common.exception;

import bank_binding.common.enums.ReasonEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessLogicException extends RuntimeException {

  private static final long serialVersionUID = -1700954102917630739L;

  private Integer code;
  private String message;
  private Object data;
  private ExceptionType exceptionType = ExceptionType.WARN;

  public BusinessLogicException(Integer code, String message) {
    super(message);
    this.code = code;
    this.message = message;
  }

  public BusinessLogicException(ReasonEnum reasonEnum) {
    super(reasonEnum.getMessage());
    this.code = reasonEnum.getValue();
    this.message = reasonEnum.getMessage();
  }

  public BusinessLogicException(Integer code, Object data, String message) {
    super(message);
    this.code = code;
    this.data = data;
    this.message = message;
  }

  @Override
  public String toString() {
    return message != null ? message : "NO_MESSAGE";
  }

  @Override
  public synchronized Throwable fillInStackTrace() {
    return null;
  }
}
