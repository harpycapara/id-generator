package common.exception;

import bank_binding.common.enums.ReasonEnum;

/**
 * Using for invalid request model params using in domain business logic for response
 *
 * @author tuannlh
 */
public final class InvalidParamException extends ServiceException {

  private static final long serialVersionUID = 1144063804703956023L;

  public InvalidParamException(String message) {
    super(ReasonEnum.INVALID_PARAMS, String.format("%s: %s", ReasonEnum.INVALID_PARAMS.name(), message));
  }

}
