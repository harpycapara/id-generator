package common.result;

import io.vavr.control.Either;

import javax.annotation.Nonnull;

/**
 * @author trivm2
 */
public interface ZEither {
  static <R> Either<ZError, R> notFound(@Nonnull String msgFormat, Object... args) {
    return _makeLeft(ZCommonCode.NOT_FOUND, null, msgFormat, args);
  }

  static <R> Either<ZError, R> invalidParams(@Nonnull String msgFormat, Object... args) {
    return _makeLeft(ZCommonCode.INVALID_PARAMS, null, msgFormat, args);
  }

  static <R> Either<ZError, R> exception(Exception origException) {
    return _makeLeft(ZCommonCode.Exception, origException, null, null);
  }

  static <R> Either<ZError, R> exception(Throwable origException, String msgFormat, Object... args) {
    return _makeLeft(ZCommonCode.Exception, origException, msgFormat, args);
  }

  static <R> Either<ZError, R> _makeLeft(@Nonnull ZCommonCode code,
                                         Throwable origException,
                                         String msgFormat,
                                         Object args) {
    return _makeLeft(code.getValue(), origException, msgFormat, args);
  }

  static <R> Either<ZError, R> _makeLeft(int code,
                                         Throwable origException,
                                         String msgFormat,
                                         Object args) {
    String message;
    if (msgFormat != null) {
      try {
        message = String.format(msgFormat, args);
      } catch (Exception ex) {
        message = "WRONG STRING FORMAT! " + ex.getMessage();
      }
    } else {
      message = origException.getMessage();
    }

    return Either.left(new ZError(code, message, origException));

  }

  static <R> Either<ZError, R> left(int errCode) {
    return Either.left(new ZError(errCode));
  }

  static <R> Either<ZError, R> left(int errCode, String message) {
    return Either.left(new ZError(errCode, message));
  }

  static <R> Either<ZError, R> left(int errCode, String message, Throwable ex) {
    return Either.left(new ZError(errCode, message, ex));
  }

  static <R> Either<ZError, R> left(ZError l) {
    return Either.left(l);
  }

  static <R> Either<ZError, R> right(R r) {
    return Either.<ZError, R>right(r);
  }

}
