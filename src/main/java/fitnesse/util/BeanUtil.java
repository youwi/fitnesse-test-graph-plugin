package fitnesse.util;

import com.google.gson.Gson;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * pluginTestGraph
 * Created by yu on 2018/7/19.
 */
public class BeanUtil {
  /**
   * "company.client.id";
   *
   * @param str
   * @throws Exception
   */
  public static Object getObjectByPath(Object object, String str) throws Exception {
    String[] fieldNames = str.split("\\.");

    Field field;
    for (String fieldName : fieldNames) {
      field = getFieldByName(object.getClass(), fieldName);
      object = getFieldValue(object, field);
    }
    return object;
  }

  public static Object getFieldValue(Object obj, Field field) throws Exception {
    field.setAccessible(true);
    return field.get(obj);
  }

  public static Field getFieldByName(Class<?> targetClass, String fieldName) throws Exception {
    Field field = null;
    try {
      field = targetClass.getDeclaredField(fieldName);
    } catch (Exception e) {

    }
    if (field != null) {
      return field;
    }
    try {
      field = targetClass.getSuperclass().getDeclaredField(fieldName);
    } catch (Exception e) {

    }
    if (field != null) {
      return field;
    }
    try {
      field = targetClass.getField(fieldName);
    } catch (Exception e) {

    }

    return field;
  }

  public static String objectToJson(Object object) {
    try {
      return new Gson().toJson(object);
    } catch (Exception e) {

    }
    if (object instanceof Map) {
      return new JSONObject(object).toString();
    }
    JSONObject jsonObject = new JSONObjectEx(object);
    return jsonObject.toString();
  }

  /**
   * 获取线程栈上的变量的变量.
   * 语法: ClassName.varName
   * //TODO
   *
   * @return object
   */
  public static Object getStackObject(String path) {
    String className = path.split(".")[0];
    String varName = path.split(".")[1];
    for (StackTraceElement stc : Thread.currentThread().getStackTrace()) {
      if (stc.getClassName().equals(className)) {
        stc.getFileName();
      }

    }
    ;
    return null;
  }
}

