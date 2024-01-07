package com.example.shoestore.sercurity.exception;

import com.example.shoestore.infrastructure.utils.MessageUtils;
import org.springframework.security.core.AuthenticationException;

public class UserDetailNotfoundException extends AuthenticationException {

    public UserDetailNotfoundException(String msg) {
        super(MessageUtils.getMessage(msg));
    }
}
