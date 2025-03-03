package common.exception;

import bank_binding.common.enums.ReasonEnum;

/**
 * Using for authorization failed
 *
 * @author tuannlh
 */
public final class AuthorizationFailedException extends ServiceException {

  public AuthorizationFailedException(String message) {
    super(ReasonEnum.PERMISSION_DENIED, message);
  }
}
