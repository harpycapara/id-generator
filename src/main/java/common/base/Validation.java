package common.base;

import io.vavr.control.Either;

public interface Validation<T> {
  Either<Result.Error, Boolean> validate(T args);
}
