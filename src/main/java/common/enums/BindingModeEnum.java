package common.enums;

import bank_model.v1.proto.BindingModeType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum BindingModeEnum {
  UNK_BM("UNK_BM", "unknown binding mode", BindingModeType.UNK_BM_VALUE),
  A("A", "user flow on ZaloPay site", BindingModeType.A_VALUE),
  B("B", "user flow on bank site", BindingModeType.B_VALUE),
  C("C", "user flow on both ZaloPay site and bank site", BindingModeType.C_VALUE),
  D("D", "user flow on bank counter, internet banking", BindingModeType.D_VALUE);

  private static final Map<String, BindingModeEnum> VALUE_MAP = new HashMap<>();
  private static final Map<Integer, BindingModeEnum> PROTO_VALUE_MAP = new HashMap<>();

  static {
    for (BindingModeEnum item : BindingModeEnum.values()) {
      VALUE_MAP.put(item.value, item);
      PROTO_VALUE_MAP.put(item.protoValue, item);
    }
  }

  private final String value;
  private final String description;
  private final int protoValue;

  BindingModeEnum(String value, String description, int protoValue) {
    this.value = value;
    this.description = description;
    this.protoValue = protoValue;
  }

  public static BindingModeType getBindingModeType(String value) {
    return BindingModeType.forNumber(VALUE_MAP.get(value).getProtoValue());
  }

  public static String getValue(Integer protoValue) {
    return PROTO_VALUE_MAP.get(protoValue).getValue();
  }
}
