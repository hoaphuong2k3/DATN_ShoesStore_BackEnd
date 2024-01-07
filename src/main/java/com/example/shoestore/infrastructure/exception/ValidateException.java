package com.example.shoestore.infrastructure.exception;

import com.example.shoestore.infrastructure.utils.MessageUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValidateException extends Exception {

    private Object data;

    public ValidateException(String message) {
        super(MessageUtils.getMessage(message));
    }

    public ValidateException(String message, String data) {
        super(MessageUtils.getMessage(message, MessageUtils.getMessage(data)));
        this.data = data;
    }

}
