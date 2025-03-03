package common.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum RedisKeyEnum {
  BANK_CONFIG("bank-config"),
  BANK_FUNCTION("bank-function"),
  BANK_BIN("bank-bin"),
  CARD_ENTITY("card-entity"),
  ACCOUNT_ENTITY("account-entity"),
  BINDING_BANK_GLOBAL("BINDING_BANK_GLOBAL"),
  FORCE_REMOVE("FORCE_REMOVE"),
  ;

  private static final Map<String, RedisKeyEnum> REDIS_KEY_ENUM_MAP = new HashMap<>();

  static {
    for (RedisKeyEnum redisKeyEnum : RedisKeyEnum.values()) {
      REDIS_KEY_ENUM_MAP.put(redisKeyEnum.value, redisKeyEnum);
    }
  }

  private final String value;

  RedisKeyEnum(String value) {
    this.value = value;
  }
}
