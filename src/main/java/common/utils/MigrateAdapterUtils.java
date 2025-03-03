package common.utils;

import bank_binding.common.enums.BankServiceEnum;
import bank_binding.common.enums.BindingModeEnum;
import bank_binding.common.enums.PaymentInstrumentEnum;
import bank_binding.domain.constant.FlowLinkFromEnum;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

import static bank_binding.common.enums.IntegratedChannelEnum.*;

public class MigrateAdapterUtils {

  public static String makeIntegrationId(String bankCode,
                                         String bankConnCode,
                                         Integer pmcID, String flowFrom) {
    String tpeBankCode = refactorBankCode(bankCode, bankConnCode);
    String paymentInstrument = getPaymentInstrument(pmcID);
    BindingModeEnum bindingMode;
    // force mode D if flow sync from obs with flow from Bank
    if (FlowLinkFromEnum.BANK_BC.getValue().equals(flowFrom)) {
      bindingMode = BindingModeEnum.D;
    } else {
      bindingMode = getBindingMode(bankCode,
                                   bankConnCode,
                                   paymentInstrument);
    }

    String integratedChannel = getIntegratedChannel(bankCode,
                                                    bankConnCode,
                                                    pmcID,
                                                    paymentInstrument);

    return tpeBankCode + "|" +
        integratedChannel + "|" +
        paymentInstrument + "|" +
        bindingMode;
  }

  private static String getPaymentInstrument(int pmcID) {
    switch (pmcID) {
      case 36:
        return PaymentInstrumentEnum.CC.getValue();
      case 37:
        return PaymentInstrumentEnum.ACC.getValue();
      case 39:
        return PaymentInstrumentEnum.ATM.getValue();
      case 41:
        return PaymentInstrumentEnum.CCDB.getValue();
      default:
        return PaymentInstrumentEnum.UNK_PI.getValue();
    }
  }

  private static final Set<String> CCDB_MODE_C_BANK_CONNECTOR_CODES = ImmutableSet.of("ZPSTB2", "ZPVPBC", "ZPCS", "");

  private static BindingModeEnum getBindingMode(String bankCode,
                                               String bankConnectorCode,
                                               String paymentInstrument) {
    if (bankConnectorCode.equals("ZPNAPAS3")) {
      return BindingModeEnum.C;
    }

    if (PaymentInstrumentEnum.CC.getValue().equals(paymentInstrument)) {
      if ("ZPMSB2".equals(bankConnectorCode)) {
        return BindingModeEnum.A;
      }
      return BindingModeEnum.C;
    }

    if (PaymentInstrumentEnum.CCDB.getValue().equals(paymentInstrument)) {
      if (CCDB_MODE_C_BANK_CONNECTOR_CODES.contains(bankConnectorCode)) {
        return BindingModeEnum.C;
      }
      return BindingModeEnum.A;
    }

    switch (bankCode) {
      case "VTB":

      case "EIB":
      case "SGCB":
      case "MB":
      case "VCCB":
      case "VIB":
      case "HDB":
      case "MSB":
      case "NAB":
      case "OCB":
      case "PVCB":
      case "OJB":
      case "SHBVN":
        return BindingModeEnum.A;
      case "VCB":
        if (PaymentInstrumentEnum.ACC.getValue().equals(paymentInstrument)
            && !bankConnectorCode.equals("ZPVCB2")) {
          return BindingModeEnum.B;
        }
        return BindingModeEnum.A;
      case "VARB":
        if (PaymentInstrumentEnum.ATM.getValue().equals(paymentInstrument) || PaymentInstrumentEnum.ACC.getValue().equals(paymentInstrument)) {
          return BindingModeEnum.A;
        }
        return BindingModeEnum.D;
      case "VPB":
        if ("ZPVPBC".equals(bankConnectorCode)) {
          return BindingModeEnum.C;
        }
        if ("ZPVPB2".equals(bankConnectorCode)) {
          return BindingModeEnum.A;
        }
        return BindingModeEnum.B;
      case "TPB":
        if (paymentInstrument.equals(PaymentInstrumentEnum.ATM.getValue())) {
          return BindingModeEnum.C;
        }
        return BindingModeEnum.D;
      case "CC":
      case "CCDB":
        return BindingModeEnum.C;
      case "SCB":
        if ("ZPSTB2".equals(bankConnectorCode)) {
          return BindingModeEnum.C;
        }
        return BindingModeEnum.A;
      case "BIDV":
        if (PaymentInstrumentEnum.ACC.getValue().equals(paymentInstrument)
            && bankConnectorCode.isEmpty()) {
          return BindingModeEnum.C;
        }
        return BindingModeEnum.A;
      case "SGB":
        return BindingModeEnum.A;
      case "SEAB":
        return BindingModeEnum.A;
      case "TCB":
        if (PaymentInstrumentEnum.ACC.getValue().equals(paymentInstrument)) {
          return BindingModeEnum.B;
        }
        return BindingModeEnum.A;
      case "ACB":
        if (paymentInstrument.equals(PaymentInstrumentEnum.ACC.getValue())) {
          return BindingModeEnum.A;
        }
        return BindingModeEnum.A;
      case "WOO":
        if(bankConnectorCode.equals("ZPWOO")){
          return BindingModeEnum.A;
        }
      default:
        return BindingModeEnum.UNK_BM;
    }
  }

  public static BankServiceEnum getService(Integer appID, Integer transType) {
    if (appID == -1 && transType == 1) {
      return BankServiceEnum.PAYMENT;
    } else if (appID == 454 && transType == 1) {
      return BankServiceEnum.TOPUP;
    } else if (appID == 452 && transType == 1) {
      return BankServiceEnum.WITHDRAW;
    } else if (appID == 450 && transType == 4) {
      return BankServiceEnum.TRANSFER;
    } else {
      return BankServiceEnum.UNKNOWN_BS;
    }
  }

  private static String getIntegratedChannel(String bankCode,
                                            String bankConnCode,
                                            Integer pmcId,
                                            String paymentInstrument) {
    if (PaymentInstrumentEnum.CC.getValue().equals(paymentInstrument)
        || PaymentInstrumentEnum.CCDB.getValue().equals(paymentInstrument)) {
      if ("ZPSTB".equals(bankConnCode) || "ZPSTB2".equals(bankConnCode)) {
        return D_STB.getValue();
      } else if ("ZPVIB".equals(bankConnCode)) {
        return D_VIB.getValue();
      } else if ("ZPVCBC".equals(bankConnCode)) {
        return D_VCB.getValue();
      } else if ("ZPVPBC".equals(bankConnCode) || "ZPVPB2".equals(bankConnCode)) {
        return D_VPB.getValue();
      } else if ("ZPTCBC".equals(bankConnCode)) {
        return D_TCB.getValue();
      } else if ("ZPMSB2".equals(bankConnCode)) {
        return D_MSB.getValue();
      } else {
        return CYBS.getValue();
      }
    }
    if ("ZPNAPAS3".equals(bankConnCode)) {
      return NAPAS.getValue();
    }

    switch (bankCode) {
      case "VARB":
        return D_AGB.getValue();
      case "BIDV":
        if (39 == pmcId) {
          return D_BIDVC.getValue();
        }
        return D_BIDV.getValue();
      case "VTB":
        return D_CTG.getValue();
      case "EIB":
        return D_EIB.getValue();
      case "HDB":
        return D_HDB.getValue();
      case "MB":
        return D_MB.getValue();
      case "MSB":
        return D_MSB.getValue();
      case "NAB":
        return D_NAB.getValue();
      case "OCB":
        return D_OCB.getValue();
      case "OJB":
        return D_OJB.getValue();
      case "PVCB":
        return D_PVCB.getValue();
      case "SGCB":
        return D_SGCB.getValue();
      case "SCB":
        return D_STB.getValue();
      case "SHBVN":
        return D_SHBVN.getValue();
      case "TPB":
        return D_TPB.getValue();
      case "VCB":
        return D_VCB.getValue();
      case "VCCB":
        if ("ZPVCCBFA".equals(bankConnCode)) {
          return D_VCCB_FA.getValue();
        }
        return D_VCCB.getValue();
      case "VIB":
        return D_VIB.getValue();
      case "VPB":
        return D_VPB.getValue();
      case "SGB":
        return D_SGB.getValue();
      case "SEAB":
        return D_SEAB.getValue();
      case "TCB":
        return D_TCB.getValue();
      case "ACB":
        return D_ACB.getValue();
      case "WOO":
        return D_WOO.getValue();
      default:
        return UNKNOWN_IC.getValue();
    }
  }

  private static String refactorBankCode(String bankCode, String bankConnCode) {
    if ("CCDB".equals(bankCode)) {
      switch (bankConnCode) {
        case "ZPSTB":
        case "ZPSTB2":
          return "SCB";
        case "ZPVIB":
          return "VIB";
        case "ZPVCBC":
          return "VCB";
        case "ZPVPBC":
        case "ZPVPB2":
          return "VPB";
        case "ZPTCBC":
          return "TCB";
        case "ZPMSB2":
          return "MSB";
      }
    } else if ("CC".equals(bankCode)) {
      switch (bankConnCode) {
        case "ZPMSB2":
          return "MSB";
      }
    }
    return bankCode;
  }

  public static String getBankCodeFromIntegrationId(String integrationId) {
    return integrationId.split("\\|")[0];
  }

  public static String getIntegratedChannelFromIntegrationId(String integrationId) {
    return integrationId.split("\\|")[1];
  }

  public static String getPaymentInstrumentFromIntegrationId(String integrationId) {
    return integrationId.split("\\|")[2];
  }

  public static String getBindingModeFromIntegrationId(String integrationId) {
    return integrationId.split("\\|")[3];
  }
}