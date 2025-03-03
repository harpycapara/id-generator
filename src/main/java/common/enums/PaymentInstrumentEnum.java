package common.enums;

import bank_model.v1.proto.PaymentInstrumentType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum PaymentInstrumentEnum {

  UNK_PI("UNK_PI", "Unknown Payment Instrument", PaymentInstrumentType.UNK_PI_VALUE),
  ACC("ACC", "Bank Account", PaymentInstrumentType.ACC_VALUE),
  ATM("ATM", "Domestic Debit Card", PaymentInstrumentType.ATM_VALUE),
  DCC("DCC", "Domestic Credit Card", PaymentInstrumentType.DCC_VALUE),
  CCDB("CCDB", "International Debit Card", PaymentInstrumentType.CCDB_VALUE),
  CC("CC", "International Credit Card", PaymentInstrumentType.CC_VALUE),
  PRE("PRE", "International Prepaid Card", PaymentInstrumentType.PRE_VALUE);

  private static final Map<String, PaymentInstrumentEnum> VALUE_MAP = new HashMap<>();
  private static final Map<Integer, PaymentInstrumentEnum> PROTO_VALUE_MAP = new HashMap<>();

  static {
    for (PaymentInstrumentEnum item : PaymentInstrumentEnum.values()) {
      VALUE_MAP.put(item.value, item);
      PROTO_VALUE_MAP.put(item.protoValue, item);
    }
  }

  private final String value;
  private final String description;
  private final int protoValue;

  PaymentInstrumentEnum(String value, String description, int protoValue) {
    this.value = value;
    this.description = description;
    this.protoValue = protoValue;
  }

  public static PaymentInstrumentType getPaymentInstrumentType(String value) {
    return PaymentInstrumentType.forNumber(VALUE_MAP.get(value).getProtoValue());
  }

  public static String getValue(Integer protoValue) {
    return PROTO_VALUE_MAP.get(protoValue).getValue();
  }
}
