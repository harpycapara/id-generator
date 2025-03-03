package common.http;

import bank_binding.common.exception.ServiceException;

import java.util.Collections;

/**
 * @author tuannlh
 */
public class ExceptionBuilder {

  public static ErrorInfo getFrom(ServiceException ex) {
    ErrorInfo errorInfo = new ErrorInfo();
    errorInfo.setMessage(ex.getReasonMessage());

    ErrorInfo.Details details = new ErrorInfo.Details();
    details.setDomain(ex.getDomain());
    details.setReason(String.valueOf(ex.getReason().getValue()));
    details.setMetadata(ex.getMetadata());
    errorInfo.setDetails(Collections.singletonList(details));
    return errorInfo;
  }
}
