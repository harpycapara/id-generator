package common.utils;

import javax.annotation.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: chucvv
 * Date: 01/08/2021
 */
public class Strings {
  public static boolean isEmpty(@Nullable Object str) {
    return str == null || "".equals(str);
  }

  public static String getOrEmpty(@Nullable String str) {
    return str == null ? "" : str;
  }

  // @formatter:off
  static final byte[] HEX_CHAR_TABLE = {
      (byte) '0', (byte) '1', (byte) '2', (byte) '3',
      (byte) '4', (byte) '5', (byte) '6', (byte) '7',
      (byte) '8', (byte) '9', (byte) 'a', (byte) 'b',
      (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f'
  };
  // @formatter:on

  /**
   * Convert a byte array to a hexadecimal string
   *
   * @param raw A raw byte array
   * @return Hexadecimal string
   */
  public static String byteArrayToHexString(byte[] raw) {
    byte[] hex = new byte[2 * raw.length];
    int index = 0;

    for (byte b : raw) {
      int v = b & 0xFF;
      hex[index++] = HEX_CHAR_TABLE[v >>> 4];
      hex[index++] = HEX_CHAR_TABLE[v & 0xF];
    }
    return new String(hex);
  }

  public static String upperCamelCase(String raw) {
    if (raw == null || raw.isEmpty()) {
      return raw;
    }
    return raw.substring(0, 1).toUpperCase() + raw.substring(1);
  }

}
