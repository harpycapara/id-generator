package common.exception;

import bank_binding.common.enums.ReasonEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ServiceException extends RuntimeException {

  private static final long serialVersionUID = 1778870314635314159L;

  private ReasonEnum reason;
  private String reasonMessage;
  private String domain = "bank-binding";
  private Map<String, String> metadata;

  public ServiceException(Throwable cause) {
    super(cause);
    this.reason = ReasonEnum.UNDEFINED;
  }

  public ServiceException(ReasonEnum reason) {
    super(reason.getMessage());
    this.reason = reason;
    this.reasonMessage = reason.getMessage();
  }

  public ServiceException(ReasonEnum reason, String message) {
    super(message);
    this.reason = reason;
    this.reasonMessage = message;
  }

  public ServiceException(ReasonEnum reason, String message, Throwable cause) {
    super(message, cause);
    this.reason = reason;
    this.reasonMessage = message;
  }

  public ServiceException(ReasonEnum reason, Throwable cause) {
    super(cause);
    this.reason = reason;
    this.reasonMessage = cause.getMessage();
  }

}
