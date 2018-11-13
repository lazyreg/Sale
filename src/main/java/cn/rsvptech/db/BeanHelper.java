package cn.rsvptech.db;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuzhiyong on 2017/11/7.
 */
public final class BeanHelper {
  public static <T> Map<String, String> bean2Map(T t, boolean needAnnotation) {
    String[][] fieldNames = DataBaseHelper.getFieldNames(t.getClass());
    Map<String, String> result = new HashMap<>();
    for (String[] fields : fieldNames) {
      String fieldName = fields[0];
      String annotationName = fields[1];
      if (needAnnotation) {
        try {
          result.put(annotationName, BeanUtils.getProperty(t, fieldName));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
          e.printStackTrace();
        }
      } else {
        try {
          result.put(fieldName, BeanUtils.getProperty(t, fieldName));
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    }
    return result;
  }
}
