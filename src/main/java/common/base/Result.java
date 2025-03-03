package common.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class Result<T> {
  @Getter
  @RequiredArgsConstructor
  public static class Error {
    private final Exception exception;
  }

  @Getter
  @RequiredArgsConstructor
  public
  class Data {
    private final T data;
  }
}
