package common.enums;

import bank_model.v1.proto.BankServiceType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum BankServiceEnum {

  UNKNOWN_BS("UNKNOWN_BS", "Unknown", BankServiceType.UNK_BS_VALUE),
  TOPUP("TOPUP", "Topup", BankServiceType.TOPUP_VALUE),
  WITHDRAW("WITHDRAW", "Withdraw", BankServiceType.WITHDRAW_VALUE),
  TRANSFER("TRANSFER", "Transfer", BankServiceType.TRANSFER_VALUE),
  PAYMENT("PAYMENT", "Payment", BankServiceType.PAYMENT_VALUE);

  private static final Map<String, BankServiceEnum> VALUE_MAP = new HashMap<>();
  private static final Map<Integer, BankServiceEnum> PROTO_VALUE_MAP = new HashMap<>();

  static {
    for (BankServiceEnum item : BankServiceEnum.values()) {
      VALUE_MAP.put(item.value, item);
      PROTO_VALUE_MAP.put(item.protoValue, item);
    }
  }

  private final String value;
  private final String description;
  private final int protoValue;

  BankServiceEnum(String value, String description, int protoValue) {
    this.value = value;
    this.description = description;
    this.protoValue = protoValue;
  }

  public static String getValue(int protoValue) {
    return PROTO_VALUE_MAP.get(protoValue).getValue();
  }

  public static BankServiceType getBankServiceType(String value) {
    return BankServiceType.forNumber(VALUE_MAP.get(value).getProtoValue());
  }
}
