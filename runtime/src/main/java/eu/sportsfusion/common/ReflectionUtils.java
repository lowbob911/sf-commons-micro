package eu.sportsfusion.common;

import java.lang.reflect.Field;

/**
 * @author Igor Vashyst
 */
public class ReflectionUtils {

    public static Field findField(final Class clazz, final String name) {
        Class target = clazz;

        while (target != null && !Object.class.equals(target)) {
            Field[] fields = target.getDeclaredFields();

            for (Field f : fields) {
                if (name.equals(f.getName())) {
                    return f;
                }
            }

            // Look for the field in the superclass.
            target = target.getSuperclass();
        }

        return null;
    }
}
