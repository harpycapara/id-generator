package common.enums;

import lombok.Getter;

@Getter
public enum ActionEnum {
  NOTHING(0,"khong lam gi het"),
  VERIFY_OTP(1,"xac thuc otp/token"),
  OPEN_WEB_BANK(2,"link den site ngan hang"),
  ADD_USER_INFO(3,"dien them thong tin bo sung cho flow mo tai khoan ngan hang");

  private int actionType;
  private String description;
  ActionEnum(int actionType, String description) {
    this.actionType = actionType;
    this.description = description;
  }

}
