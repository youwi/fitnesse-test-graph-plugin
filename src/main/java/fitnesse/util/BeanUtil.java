package fitnesse.util;

import java.lang.reflect.Field;

/**
 * pluginTestGraph
 * Created by yu on 2018/7/19.
 */
public class BeanUtil {
    /**
     *       "company.client.id";
     * @param str
     * @throws Exception
     */
    public static Object getObjectByPath(Object object,String str) throws Exception {
        String[] fieldNames = str.split("\\.");

        Field field;

        Class<?> targetClass = object.getClass();

        for (String fieldName: fieldNames) {

            field =  getFieldByName(targetClass, fieldName);
            targetClass = field.getType();
            object = getFieldValue(object, field);

        }
        return object;
    }

    public static Object getFieldValue(Object obj, Field field) throws Exception {
        field.setAccessible(true);
        return field.get(obj);
    }

    public static Field getFieldByName(Class<?> targetClass, String fieldName)
            throws Exception {
        return targetClass.getDeclaredField(fieldName);
    }
}

