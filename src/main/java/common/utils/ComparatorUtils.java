package common.utils;

import java.util.Comparator;
import java.util.List;

public class ComparatorUtils {

  /**
   *
   * @param priorityList: first element has highest priority
   * @return
   * @param <T>
   */
  public static <T> Comparator<T> comparatorByPriorityList(List<T> priorityList) {
    return (o1, o2) -> {
      int index1 = priorityList.indexOf(o1);
      int index2 = priorityList.indexOf(o2);
      if (index1 == -1 && index2 == -1) {
        return 0;
      }
      if (index1 == -1) {
        return 1;
      }
      if (index2 == -1) {
        return -1;
      }
      return Integer.compare(index1, index2);
    };
  }

}
