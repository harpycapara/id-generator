package common.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;

/**
 * @author TriLT2
 * @version 1.0
 * @date 15/07/2021
 */
@Component
public class ValidateUtils {

  public boolean checkLuhn(String cardNo) {
    int nDigits = cardNo.length();

    int nSum = 0;
    boolean isSecond = false;
    for (int i = nDigits - 1; i >= 0; i--) {

      int d = cardNo.charAt(i) - '0';

      if (isSecond)
        d = d * 2;

      // We add two digits to handle
      // cases that make two digits
      // after doubling
      nSum += d / 10;
      nSum += d % 10;

      isSecond = !isSecond;
    }
    return (nSum % 10 == 0);
  }

  private final DateTimeFormatter formatter = DateTimeFormatter
      .ofPattern("MMyy");

  public LocalDate parseDate(String dateStr) {
    return YearMonth
        .parse(dateStr, formatter)
        .atDay(1);
  }

  public static <T> T getOrDefault(T value, Predicate<T> validator, T defaultValue) {
    return validator.test(value) ? value : defaultValue;
  }

  public static String getOrDefault(String value, String defaultValue) {
    return getOrDefault(value, str -> str != null && !str.isEmpty(), defaultValue);
  }

}
