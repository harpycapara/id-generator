package common.utils;

import bank_model.v1.proto.PaymentInstrumentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IntegrationIdUtil {

  public static final String DELIMITER_PATTERN = "\\|";

  public static @Nullable String getBankCode(@NotNull String integrationId) {
    String[] params = integrationId.split(DELIMITER_PATTERN);
    return params.length > 0 ? params[0] : null;
  }

  public static @Nullable String getIntegrateChannel(@NotNull String integrationId) {
    String[] params = integrationId.split(DELIMITER_PATTERN);
    return params.length > 1 ? params[1] : null;
  }

  public static @Nullable String getPaymentInstrument(@NotNull String integrationId) {
    String[] params = integrationId.split(DELIMITER_PATTERN);
    return params.length > 2 ? params[2] : null;
  }

  public static @Nullable PaymentInstrumentType getPaymentInstrumentAsProto(@NotNull String integrationId) {
    String paymentInstrumentStr = getPaymentInstrument(integrationId);
    if (paymentInstrumentStr == null) {
      return null;
    }
    PaymentInstrumentType paymentInstrument = PaymentInstrumentType.valueOf(paymentInstrumentStr);
    return paymentInstrument.getNumber() < 1 ? null : paymentInstrument;
  }

  public static String getBindingMode(@NotNull String integrationId) {
    String[] params = integrationId.split(DELIMITER_PATTERN);
    return params.length > 3 ? params[3] : null;
  }

  public static String getBankConnectorCode(@NotNull String integrationId) {
    String[] params = integrationId.split(DELIMITER_PATTERN);
    return params.length > 4 ? params[4] : null;
  }

}
