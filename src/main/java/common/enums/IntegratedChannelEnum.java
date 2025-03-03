package common.enums;

import java.util.HashMap;

public enum IntegratedChannelEnum {
  UNKNOWN_IC("Unknown IC", "Unknown IntegratedChannel"),
  D_AGB("D-AGB", "Direct AGB"),
  D_BIDV("D-BIDV", "Direct BIDV"),
  D_BIDVC("D-BIDVC", "Direct BIDVC"),
  CYBS("CYBS", "Cybersource"),
  D_CTG("D-CTG", "Direct CTG"),
  D_DAB("D-DAB", "Direct DAB"),
  D_EIB("D-EIB", "Direct EIB"),
  D_HDB("D-HDB", "Direct HDB"),
  D_MB("D-MB", "Direct MB"),
  D_MSB("D-MSB", "Direct MSB"),
  D_NAB("D-NAB", "Direct NAB"),
  NAPAS("NAPAS", "Napas"),
  D_OCB("D-OCB", "Direct OCB"),
  D_OJB("D-OJB", "Direct OJB"),
  D_PVCB("D-PVCB", "Direct PVCB"),
  D_SGCB("D-SGCB", "Direct SGCB"),
  D_STB("D-STB", "Direct STB"),
  D_SHBVN("D-SHBVN", "Direct SHBVN"),
  D_TPB("D-TPB", "Direct TPB"),
  D_VCB("D-VCB", "Direct VCB"),
  D_VCBC("D-VCBC", "Direct VCBC"),
  D_VCCB("D-VCCB", "Direct VCCB"),
  D_VCCB_FA("D-VCCB-FA", "Direct VCCB FA"),
  D_VIB("D-VIB", "Direct VIB"),
  D_VPB("D-VPB", "Direct VPB"),
  D_TCB("D-TCB", "Direct TCB"),
  D_SEAB("D-SEAB", "Direct SEAB"),
  D_SGB("D-SGB", "Direct SGB"),
  D_ACB("D-ACB", "Direct ACB"),
  D_WOO("D-WOO","Direct WOO");
  private static final HashMap<String, IntegratedChannelEnum> returnMap =
      new HashMap<>();

  static {
    for (IntegratedChannelEnum returnCodeEnum : IntegratedChannelEnum.values()) {
      returnMap.put(returnCodeEnum.value, returnCodeEnum);
    }
  }

  private final String value;
  private final String description;

  IntegratedChannelEnum(String value, String description) {
    this.value = value;
    this.description = description;
  }

  public String getValue() {
    return value;
  }
}
