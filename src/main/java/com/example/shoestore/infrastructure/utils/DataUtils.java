package com.example.shoestore.infrastructure.utils;

import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.constants.RegexPattern;
import com.example.shoestore.infrastructure.exception.ValidateException;
import io.micrometer.common.util.StringUtils;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

public class DataUtils {

    public static <T> boolean isNull(T value) {
        return value == null;
    }

    public static <T> boolean isNotNull(T value) {
        return value != null;
    }

    public static boolean isNotBlank(String value) {
        return StringUtils.isNotBlank(value);
    }

    public static boolean isBlank(String value) {
        return StringUtils.isBlank(value);
    }

    public static <T> boolean isEmpty(Collection collection) {
        return CollectionUtils.isEmpty(collection);
    }

    public static <T> boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    public static <K, V> boolean isNotEmpty(Map<K, V> inputMap) {
        return MapUtils.isNotEmpty(inputMap);
    }

    public static boolean isIncorrectDateFormat(String dateString) {
        return !Pattern.matches(RegexPattern.DATE_FORMAT_REGEX, dateString);
    }

    @SneakyThrows
    public static void validateStringDate(String fromDate, String toDate) {
        if (DataUtils.isNotBlank(fromDate) && DataUtils.isNotBlank(toDate)) {
            if (DataUtils.isIncorrectDateFormat(fromDate)) {
                throw new ValidateException(MessageCode.Commom.DATE_INCORRECT_FORMAT);
            }
            if (DataUtils.isIncorrectDateFormat(toDate)) {
                throw new ValidateException(MessageCode.Commom.DATE_INCORRECT_FORMAT);
            }
        }
        if (DataUtils.isNotBlank(fromDate)) {
            if (DataUtils.isIncorrectDateFormat(fromDate)) {
                throw new ValidateException(MessageCode.Commom.DATE_INCORRECT_FORMAT);
            }
        }
        if (DataUtils.isNotBlank(toDate)) {
            if (DataUtils.isIncorrectDateFormat(toDate)) {
                throw new ValidateException(MessageCode.Commom.DATE_INCORRECT_FORMAT);
            }
        }
    }

    @SneakyThrows
    public static void validateFromDateAfterToDate(Date fromDate, Date toDate) {
        if (DataUtils.isNotNull(fromDate) && DataUtils.isNotNull(toDate)) {
            if (fromDate.after(toDate)) {
                throw new ValidateException(MessageUtils.getMessage(MessageCode.Commom.FROM_DATE_GT_TO_DATE));
            }
        }
    }

}
