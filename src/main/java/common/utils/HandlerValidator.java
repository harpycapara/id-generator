package common.utils;

import bank_binding.common.exception.AuthenticationFailedException;
import bank_binding.common.exception.AuthorizationFailedException;
import bank_binding.common.exception.InvalidParamException;
import bank_model.v1.proto.BindingType;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

/**
 * @author tuannlh
 */
public class HandlerValidator {

  private HandlerValidator() {throw new IllegalStateException("Utility class");}

  private static final Pattern BINDING_REQUEST_ID_PATTERN = Pattern.compile("^[0-9]{15}$");
  private static final Pattern USER_ID_PATTERN = Pattern.compile("^[0-9]{15}$");
  private static final Pattern BANK_CODE_PATTERN = Pattern.compile("^[A-Z]+$");

  public static <T> void validateElementWithValidator(String field, List<T> values,
                                                      BiConsumer<String, T> validator) {
    if (values == null) {
      return;
    }

    for (int i = 0; i < values.size(); i++) {
      validator.accept(field + "[" + i + "]", values.get(i));
    }
  }

  public static void validateUserId(String field, String value) {
    validateNotEmpty(field, value);
    validateCondition(!USER_ID_PATTERN.matcher(value).matches(), field + " is invalid");
  }

  public static void validateBindingRequestId(String field, String value) {
    validateNotEmpty(field, value);
    validateCondition(!BINDING_REQUEST_ID_PATTERN.matcher(value).matches(), field + " is invalid");
  }

  public static void validateBindingRequestId(String field, long value) {
    validateCondition(value < 100000000000000L || value > 999999999999999L, field + " is invalid");
  }

  public static void validateBankCode(String field, String value) {
    validateCondition(!BANK_CODE_PATTERN.matcher(value).matches(), field + " is invalid");
  }

  public static void validateNotEmpty(String field, String value) {
    validateCondition(Strings.isEmpty(value), field + " should not be empty");
  }

  public static void validateNotEmpty(String field, Collection<?> value) {
    validateCondition(value == null || value.isEmpty(), field + " should not be empty");
  }

  public static void validateNotNull(String field, Object value){
    validateCondition(value == null, field + " should not be null");
  }

  public static void validateNotEqualsZero(String field, int value) {
    validateCondition(value == 0, field + " is invalid");
  }

  public static void validateNotEqualsZero(String field, long value) {
    validateCondition(value == 0L, field + " is invalid");
  }

  public static void validateTrue(String field, boolean value) {
    validateCondition(!value, field + " is invalid");
  }

  public static void validateCondition(boolean conditionToFail, String messageIfFailed) {
    if (conditionToFail) {
      throw new InvalidParamException(messageIfFailed);
    }
  }

  public static void validateAuthenticationEmpty(String field, String value) {
    validateAuthenticationCondition(Strings.isEmpty(value), field + " should not be empty");
  }

  public static void validateAuthenticationCondition(boolean conditionToFail, String messageIfFailed) {
    if (conditionToFail) {
      throw new AuthenticationFailedException(messageIfFailed);
    }
  }

  public static void validateAuthorizationCondition(boolean conditionToFail, String messageIfFailed) {
    if (conditionToFail) {
      throw new AuthorizationFailedException(messageIfFailed);
    }
  }

  public static void validateBindingType(String param, BindingType bindingType) {
    validateNotNull(param, bindingType);
    validateCondition(bindingType == BindingType.UNRECOGNIZED || bindingType == BindingType.INVALID_TYPE
        , param + " is invalid");
  }

}
