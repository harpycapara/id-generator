package common.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * @author trivm2
 */
@Slf4j
public class CommonUtils {
  private static String serverIP = null;

  public CommonUtils() {
  }

  public static String formatComma(long value) {
    DecimalFormat myFormatter = new DecimalFormat("#,###");
    String output = myFormatter.format(value);
    return output;
  }

  public static String formatDot(long value) {
    try {
      DecimalFormat myFormatter = new DecimalFormat("#,###");
      String output = myFormatter.format(value);
      if (output != null) {
        output = output.replace(",", ".");
      }

      return output;
    } catch (Exception ex) {
      return String.valueOf(value);
    }
  }

  public static String getServerIP() {
    if (serverIP == null) {
      try {
        Enumeration n = NetworkInterface.getNetworkInterfaces();

        while(n.hasMoreElements()) {
          NetworkInterface e = (NetworkInterface)n.nextElement();
          Enumeration a = e.getInetAddresses();

          while(a.hasMoreElements()) {
            InetAddress addr = (InetAddress)a.nextElement();
            if (!addr.isLinkLocalAddress() && !addr.isLoopbackAddress() && addr.isSiteLocalAddress()) {
              serverIP = addr.getHostAddress();
              return serverIP;
            }
          }
        }
      } catch (Exception ex) {
        LogUtils.error("getServerIP exception " + ex.getMessage(), ex);

        try {
          serverIP = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException unknownHostException) {
          LogUtils.error("getServerIP exception " + unknownHostException.getMessage(), unknownHostException);
          serverIP = "127.0.0.1";
        }
      }
    }

    return serverIP;
  }

  public static String truncateToLength(String longString, int maxLength) {
    if (longString == null) {
      return "";
    } else {
      return longString.length() <= maxLength ? longString : longString.substring(0, maxLength);
    }
  }

  public static long getDefaultTransID() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
    long transID = Long.parseLong(sdf.format(new Date())) * 1000000000L;
    return transID;
  }

  public static String getYYYYMMfromTransID(long transID) {
    String transIDString = String.valueOf(transID);
    return getYYYYMMfromTransID(transIDString);
  }

  public static String getYYYYMMfromTransID(String transID) {
    String transIDString = String.valueOf(transID);
    if (transIDString.length() != 15) {
      throw new IllegalArgumentException("invalid transID " + transID);
    } else {
      return "20" + transIDString.substring(0, 4);
    }
  }
}
