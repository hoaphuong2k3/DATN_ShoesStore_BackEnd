package com.example.shoestore.infrastructure.utils;

public class DataBaseUtils {

    public static String getLikeCondition(String input) {
        if (DataUtils.isNotBlank(input)) {
            return "%" + sanitizeLikeValue(input) + "%";
        }
        return null;
    }


    private static String sanitizeLikeValue(String value) {
        return value.replaceAll("[\\\\_%]", "\\\\$0");
    }

}
