package common.base;

import bank_binding.common.config.ProcessTimeMonitorConfig;
import bank_binding.common.enums.ReasonEnum;
import bank_binding.common.exception.BusinessLogicException;
import bank_binding.common.utils.GsonUtils;
import bank_binding.common.utils.ProtobufUtils;
import com.google.protobuf.MessageOrBuilder;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.logging.log4j.util.StackLocatorUtil;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class RequestHandler<Req, Res extends MessageOrBuilder> {
  protected final Class<?> className;

  @Autowired
  private ValidationFactory validationFactory;
  @Autowired
  private ProcessTimeMonitorConfig processTimeMonitorConfig;

  protected RequestHandler() {
    this.className = StackLocatorUtil.getCallerClass(2);
  }

  //protected abstract Either<Result.Error, Boolean> validate(Req request);

  protected Either<Result.Error, Boolean> validate(Req request) {
    Validation validation = validationFactory.getValidationForRequest(request);
    if (validation != null) {
      return validation.validate(request);
    }
    return Either.left(new Result.Error(new BusinessLogicException(ReasonEnum.CANNOT_FIND_VALIDATION)));
  }

  protected abstract Either<Result.Error, Res> processRequest(Req request);

  protected void logRequest(Req request) {
    log.info("[{}], request={}", className.getSimpleName(), request instanceof MessageOrBuilder ?
        ProtobufUtils.print((MessageOrBuilder) request) : GsonUtils.toJson(request));
  }

  protected void logResponse(Req request, Res response) {
    log.info("[{}], response={}", className.getSimpleName(), ProtobufUtils.print(response));
  }

  public Either<Result.Error, Res> handleRequest(Req request) {
    Try.run(() -> logRequest(request)).onFailure(e -> log.error("[{}] logRequest error", className.getSimpleName(), e));
    long startTime = System.currentTimeMillis();
    return validate(request)
        .fold(Either::left, valid -> {
          val result = processRequest(request);
          result.peek(response -> Try.run(() -> {
            long endTime = System.currentTimeMillis();
            long processTime = endTime - startTime;
            if (processTime >= processTimeMonitorConfig.getBaseWarningMs()) {
              log.warn("[{}] high process time processTime={}", className.getSimpleName(), processTime);
            }
            logResponse(request, response);
          }).onFailure(e -> log.error("[{}] logResponse error", className.getSimpleName(), e)));
          return result;
        });
  }

}


