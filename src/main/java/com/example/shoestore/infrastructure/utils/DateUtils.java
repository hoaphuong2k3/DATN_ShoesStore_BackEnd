package com.example.shoestore.infrastructure.utils;

import com.example.shoestore.infrastructure.constants.DatePattern;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    public static Date strToDate(String date) {
        if (DataUtils.isBlank(date)) {
            return null;
        }
        String pattern = DatePattern.DATE;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LocalDate strToLocalDate(String date) {
        if (date == null) {
            return null;
        }

        String pattern = DatePattern.DATE;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        try {
            return LocalDate.parse(date, formatter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LocalDateTime strToLocalDateTime(String date) {
        if (date == null) {
            return null;
        }

        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        try {
            return LocalDateTime.parse(date, formatter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String localDateToStr(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }

        String pattern = DatePattern.DATE;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        try {
            return localDate.format(formatter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isNotNumber(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
