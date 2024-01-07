package com.example.shoestore.infrastructure.utils;

import com.example.shoestore.infrastructure.buldle.UTF8ResourceBundle;
import java.text.MessageFormat;
public class MessageUtils {

    public static String getMessage(String code, Object... args) {
        UTF8ResourceBundle resourceBundle = new UTF8ResourceBundle();
        String message;
        try {
            message = resourceBundle.getString(code);
            message = MessageFormat.format(message, args);
        } catch (Exception ex) {
            message = code;
        }
        return message;
    }

    public static String getMessage(String code) {
        return getMessage(code, (Object[]) null);
    }

}
