package common.utils;

import io.micrometer.core.instrument.util.StringUtils;

import java.util.StringJoiner;

public class MySQLUtils {

  public static String buildInsertQuery(String tableName, Iterable<String> cols) {
    StringJoiner colNames = new StringJoiner(",");
    StringJoiner values = new StringJoiner(",");

    for (String colName : cols) {
      colNames.add(colName);
      values.add(":" + colName);
    }

    return String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, colNames, values);
  }

  public static String getPartitionByRequestId(String tableLogPrefix, String requestIdStr) {
    if (StringUtils.isNotEmpty(requestIdStr) && requestIdStr.length() <= 15) {
      return tableLogPrefix + "20" + requestIdStr.substring(0, 4);
    } else {
      return tableLogPrefix;
    }
  }

}
