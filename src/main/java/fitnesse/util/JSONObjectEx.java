package fitnesse.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * pluginTestGraph
 * Created by yu on 2018/8/16.
 */
public class JSONObjectEx extends JSONObject {
  public JSONObjectEx() {
    super();
  }

  /**
   * 新版本不支持私有成员.
   * 这里强制支持.
   * @param obj
   */
  public JSONObjectEx(Object obj) {
    super(obj);
    Class<?> c = obj.getClass();
    for (Field field : c.getDeclaredFields()) {
      field.setAccessible(true);
      try {
        try {
          if (this.get(field.getName()) == null) {
            this.putOpt(field.getName(), field.get(obj));
          }
        } catch (JSONException e) {
//          e.printStackTrace();
          this.putOpt(field.getName(), field.get(obj));
        }
      } catch (IllegalAccessException e) {
//        e.printStackTrace();
      }
    }
  }
}
