package common.enums;

import java.util.HashMap;

public enum ServiceEnum {
  UNKNOWN_BS("UNKNOWN_BS", "Unknown"),

  TOPUP("TOPUP", "Topup"),

  WITHDRAW("WITHDRAW", "Withdraw"),

  TRANSFER("TRANSFER", "Transfer"),

  PAYMENT("PAYMENT", "Payment");

  private static final HashMap<String, ServiceEnum> returnMap =
      new HashMap<>();

  static {
    for (ServiceEnum returnCodeEnum : ServiceEnum.values()) {
      returnMap.put(returnCodeEnum.value, returnCodeEnum);
    }
  }

  private final String value;
  private final String description;

  ServiceEnum(String value, String description) {
    this.value = value;
    this.description = description;
  }

  public String getValue() {
    return value;
  }
}
