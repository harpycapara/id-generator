package common.http;

import bank_binding.common.exception.AuthenticationFailedException;
import bank_binding.common.exception.AuthorizationFailedException;
import bank_binding.common.exception.InvalidParamException;
import bank_binding.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author tuannlh
 */
@Slf4j
@ControllerAdvice
@RestController
public class HttpExceptionController extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {ServiceException.class})
  protected ResponseEntity<Object> handleGenericException(ServiceException ex, WebRequest request) {
    var errorInfo = ExceptionBuilder.getFrom(ex);
    errorInfo.setCode(HttpStatus.BAD_REQUEST.value());
    errorInfo.setMessage("This should be processed correctly");
    var errorResponse = new ErrorResponse(errorInfo);
    log.warn("Error: ", ex);
    return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }
  
  @ExceptionHandler(value = {AuthenticationFailedException.class})
  protected ResponseEntity<Object> handleAuthenticationFailed(AuthenticationFailedException ex, WebRequest request) {
    log.warn("Error: ", ex);
    var errorInfo = ExceptionBuilder.getFrom(ex);
    errorInfo.setCode(HttpStatus.UNAUTHORIZED.value());
    var errorResponse = new ErrorResponse(errorInfo);
    return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
  }

  @ExceptionHandler(value = {AuthorizationFailedException.class})
  protected ResponseEntity<Object> handleAuthorizationFailed(AuthorizationFailedException ex, WebRequest request) {
    log.warn("Error: ", ex);
    var errorInfo = ExceptionBuilder.getFrom(ex);
    errorInfo.setCode(HttpStatus.FORBIDDEN.value());
    var errorResponse = new ErrorResponse(errorInfo);
    return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
  }

  @ExceptionHandler(value = {InvalidParamException.class})
  protected ResponseEntity<Object> handleInvalidRequestParam(InvalidParamException ex, WebRequest request) {
    log.warn("Error: ", ex);
    var errorInfo = ExceptionBuilder.getFrom(ex);
    errorInfo.setCode(HttpStatus.BAD_REQUEST.value());
    var errorResponse = new ErrorResponse(errorInfo);
    return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }
}
