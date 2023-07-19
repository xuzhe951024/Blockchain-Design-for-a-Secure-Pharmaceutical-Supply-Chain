package main.java.com.rbpsc.common.utiles;

import java.lang.reflect.Field;

public class ParentToChildConvertor {
    public static <T, U> U convert(T source, Class<U> targetClass) {
        try {
            // Create a new instance of the target class
            U target = targetClass.getDeclaredConstructor().newInstance();

            // Iterate through all the fields of the source object
            for (Field sourceField : source.getClass().getDeclaredFields()) {
                // Make the field accessible if it's private
                sourceField.setAccessible(true);

                // Get the corresponding field in the target class
                Field targetField;
                try {
                    targetField = targetClass.getDeclaredField(sourceField.getName());
                } catch (NoSuchFieldException e) {
                    // If the target class doesn't have the same field, skip it
                    continue;
                }

                // Make the target field accessible if it's private
                targetField.setAccessible(true);

                // Copy the value from the source object to the target object
                targetField.set(target, sourceField.get(source));
            }

            return target;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object", e);
        }
    }
}
