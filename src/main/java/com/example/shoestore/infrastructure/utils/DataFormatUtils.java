package com.example.shoestore.infrastructure.utils;

import java.lang.reflect.Field;

public class DataFormatUtils {

    public static String formatString(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("\\s+", " ").trim();
    }

    public static void trimStringFields(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(String.class)) {
                field.setAccessible(true);
                String value = (String) field.get(obj);
                if (value != null) {
                    String trimmedValue = formatString(value);
                    field.set(obj, trimmedValue);
                }
            }
        }
    }


}
