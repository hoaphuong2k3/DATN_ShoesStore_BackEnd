package com.example.shoestore.infrastructure.exception;

import com.example.shoestore.infrastructure.utils.MessageUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResourceNotFoundException extends Exception {

    private Object data;

    public ResourceNotFoundException(String message) {
        super(MessageUtils.getMessage(message));
    }

    public ResourceNotFoundException(String message, String data) {
        super(MessageUtils.getMessage(message, MessageUtils.getMessage(data)));
    }
}
