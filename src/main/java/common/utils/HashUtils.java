package common.utils;

import bank_binding.common.exception.StatusRuntimeExceptionBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public final class HashUtils {
  public static String hashSha256ToHex(String input) {
    MessageDigest mDigest;

    try {
      mDigest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException ex) {
      LogUtils.error("Failed to hash SHA-256", ex);
      throw new StatusRuntimeExceptionBuilder().setMessage("Failed to hash SHA-256").build();
    }

    byte[] shaByteArr = mDigest.digest(input.getBytes(StandardCharsets.UTF_8));

    StringBuilder hexString = new StringBuilder();

    for (byte b : shaByteArr) {
      String hex = Integer.toHexString(255 & b);

      if (hex.length() == 1) {
        hexString.append('0');
      }

      hexString.append(hex);
    }

    return hexString.toString();
  }

  public static String hmac(String key, String input) {
    Mac hmac;
    try {
      hmac = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
      hmac.init(secretKey);
      return Hex.encodeHexString(hmac.doFinal(input.getBytes(StandardCharsets.UTF_8)));
    } catch (NoSuchAlgorithmException ex) {
      LogUtils.error("Failed to hash HmacSHA-256", ex);
      throw new StatusRuntimeExceptionBuilder().setMessage("Failed to hash HmacSHA-256").build();
    } catch (InvalidKeyException ex) {
      LogUtils.error("Invalid key exception", ex);
      throw new StatusRuntimeExceptionBuilder().setMessage("Invalid key exception").build();
    }
  }
}
