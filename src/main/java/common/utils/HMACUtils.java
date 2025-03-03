package common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;

public class HMACUtils {
  public static final String HMACMD5 = "HmacMD5";
  public static final String HMACSHA1 = "HmacSHA1";
  public static final String HMACSHA256 = "HmacSHA256";
  public static final String HMACSHA512 = "HmacSHA512";
  public static final Charset UTF8CHARSET = StandardCharsets.UTF_8;
  public static final LinkedList<String> HMACS =
      new LinkedList(Arrays.asList("UnSupport", "HmacSHA256", "HmacMD5", "HmacSHA384", "HMacSHA1", "HmacSHA512"));

  public HMACUtils() {
  }

  private static byte[] HMacEncode(String algorithm, String key, String data) {
    Mac macGenerator = null;

    try {
      macGenerator = Mac.getInstance(algorithm);
      SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
      macGenerator.init(signingKey);
    } catch (Exception var7) {
    }

    if (macGenerator == null) {
      return null;
    } else {
      byte[] dataByte = null;

      dataByte = data.getBytes(StandardCharsets.UTF_8);

      return macGenerator.doFinal(dataByte);
    }
  }

  public static String HMacBase64Encode(String algorithm, String key, String data) {
    byte[] hmacEncodeBytes = HMacEncode(algorithm, key, data);
    return hmacEncodeBytes == null ? null : (new Base64()).encodeToString(hmacEncodeBytes);
  }

  public static String hmacHexStringEncode(String algorithm, String key, String data) {
    byte[] hmacEncodeBytes = HMacEncode(algorithm, key, data);
    return hmacEncodeBytes == null ? null : Strings.byteArrayToHexString(hmacEncodeBytes);
  }
}
