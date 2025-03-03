package common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @author hunglv7
 */
public class DateTimeUtils {

  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_TIME_MILLISECONDS_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
  public static TimeZone GMT7TimeZone = TimeZone.getTimeZone("GMT+7");

  public static String getCurrentDateTime() {
    DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
    Date date = new Date();
    return dateFormat.format(date);
  }

  public static long getCurrentDate(TimeUnit unit) {
    return TimeUnit.MILLISECONDS.convert(Calendar.getInstance(GMT7TimeZone).getTimeInMillis(), unit);
  }

  public static Date getCurDateWithMilliseconds() throws ParseException {
    SimpleDateFormat formatSDF = new SimpleDateFormat(DATE_TIME_MILLISECONDS_FORMAT);
    formatSDF.setTimeZone(GMT7TimeZone);

    SimpleDateFormat parseSDF = new SimpleDateFormat(DATE_TIME_MILLISECONDS_FORMAT);
    Date date = new Date(System.currentTimeMillis());
    Date result = parseSDF.parse(formatSDF.format(date));

    return result;
  }

  public static Date convertDate(String dateStr, String format) {
    try {
      DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
      if (format != null) {
        dateFormat = new SimpleDateFormat(format);
      }
      return dateFormat.parse(dateStr);
    } catch (ParseException ex) {
    }
    return null;
  }

  public static String second2DateStr(long timeStamp, String format) {
    DateFormat dateFormat;
    if (format == null) {
      dateFormat = new SimpleDateFormat(DATE_TIME_MILLISECONDS_FORMAT);
    } else {
      dateFormat = new SimpleDateFormat(format);
    }
    return dateFormat.format(new Date(timeStamp * 1000));
  }

  public static String milliseconds2DateStr(long timeStamp, String format) {
    DateFormat dateFormat;
    if (format == null) {
      dateFormat = new SimpleDateFormat(DATE_TIME_MILLISECONDS_FORMAT);
    } else {
      dateFormat = new SimpleDateFormat(format);
    }
    return dateFormat.format(new Date(timeStamp));
  }

  public static long convertTimeStampMilliseconds(String dateStr, String format) {
    try {
      DateFormat dateFormat;
      if (format == null) {
        dateFormat = new SimpleDateFormat(DATE_TIME_MILLISECONDS_FORMAT);
      } else {
        dateFormat = new SimpleDateFormat(format);
      }
      Date date = dateFormat.parse(dateStr);
      return date.getTime();
    } catch (Exception ex) {
      return 0;
    }
  }

  public static String getCurrentTime(String format) {
    SimpleDateFormat formatSDF = new SimpleDateFormat(format);
    Date date = new Date(System.currentTimeMillis());
    return formatSDF.format(date);
  }

  public static String getCurrentDate(String format) {
    DateFormat dateFormat = new SimpleDateFormat(format);
    Date date = new Date();
    return dateFormat.format(date);
  }

  public static String convertString(Date date, String format) {
    DateFormat dateFormat;
    if (format == null) {
      dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
    } else {
      dateFormat = new SimpleDateFormat(format);
    }
    return dateFormat.format(date);
  }

  public static String timeStamp2DateStr(long timestamp) {
    if (timestamp == 0) {
      return "";
    }
    try {
      SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_MILLISECONDS_FORMAT);
      return sdf.format(new Date(timestamp));
    } catch (Exception ex) {
      return "";
    }
  }

  public static String convertTimeMilisecond2DateStr(long timestamp, String format) {
    if (timestamp == 0) {
      return "";
    }
    try {
      SimpleDateFormat sdf = new SimpleDateFormat(format);
      return sdf.format(new Date(timestamp));
    } catch (Exception ex) {
      return "";
    }
  }


  public static String convertTimeStamp2Date(long timeStamp, String format) {
    DateFormat dateFormat;
    if (format == null) {
      dateFormat = new SimpleDateFormat(DATE_TIME_MILLISECONDS_FORMAT);
    } else {
      dateFormat = new SimpleDateFormat(format);
    }
    return dateFormat.format(new Date(timeStamp * 1000));
  }

  public static Date add(Date date, int year, int month, int day, int hour, int minute, int second) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);

    cal.add(Calendar.YEAR, year);
    cal.add(Calendar.MONTH, month);
    cal.add(Calendar.DATE, day);
    cal.add(Calendar.HOUR, hour);
    cal.add(Calendar.MINUTE, minute);
    cal.add(Calendar.SECOND, second);

    return cal.getTime();
  }

  public static Date addYears(Date date, int amount) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.YEAR, amount);
    return calendar.getTime();
  }

  public static long fromUTCtoICT(long time) {
    long sevenHour = 7L * 60 * 60 * 1000;
    return time - sevenHour;
  }

  public static int getCurrentDayOfYear() {
    Calendar calendar = Calendar.getInstance();
    return calendar.get(Calendar.DAY_OF_YEAR);
  }

  public static String formatDate(SimpleDateFormat simpleDateFormat, long time) {
    if (time > 0) {
      return simpleDateFormat.format(new Date(time));
    }
    return "";
  }

  public static long howLongTo(Calendar to, TimeUnit unit) {
    Calendar now = Calendar.getInstance(GMT7TimeZone);
    long diffInMillis = to.getTimeInMillis() - now.getTimeInMillis();
    return unit.convert(diffInMillis, TimeUnit.MILLISECONDS);
  }

  public static Calendar skipTimeFromNow(long time, TimeUnit unit) {
    long skipTimeInMillis = unit.toMillis(time);
    Calendar calendar = Calendar.getInstance(GMT7TimeZone);
    calendar.setTimeInMillis(calendar.getTimeInMillis() + skipTimeInMillis);
    return calendar;
  }

  /**
   *
   * Skip hours until office time (8:00 - 17:00) and days until not weekend
   *
   */
  public static Calendar skipUntilOfficeTime(Calendar from) {
    Calendar calendar = (Calendar) from.clone();

    // Skip hours until office time (8:00 - 17:00)
    int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
    if (hourOfDay < 8) {
      calendar.set(Calendar.HOUR_OF_DAY, 8);
    } else if (hourOfDay >= 17) {
      calendar.add(Calendar.DAY_OF_YEAR, 1);
      calendar.set(Calendar.HOUR_OF_DAY, 8);
    }

    // Skip days until not weekend
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    boolean isWeekend = dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    if (isWeekend) {
      int daysUntilMonday = (Calendar.SATURDAY - dayOfWeek + 2) % 7;
      calendar.add(Calendar.DAY_OF_WEEK, daysUntilMonday);
    }

    return calendar;
  }

}
