package common.exception;

import bank_binding.common.enums.ReasonEnum;

/**
 * Using for authentication failed
 *
 * @author tuannlh
 */
public final class AuthenticationFailedException extends ServiceException {

  private static final long serialVersionUID = -7125368699233256222L;

  public AuthenticationFailedException(String message) {
    super(ReasonEnum.AUTHENTICATION_FAILED, message);
  }
}
