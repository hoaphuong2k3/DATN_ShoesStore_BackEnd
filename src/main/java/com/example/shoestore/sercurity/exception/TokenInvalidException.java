package com.example.shoestore.sercurity.exception;

import com.example.shoestore.infrastructure.utils.MessageUtils;
import org.springframework.security.authentication.BadCredentialsException;

public class TokenInvalidException extends BadCredentialsException {

    public TokenInvalidException(String msg) {
        super(MessageUtils.getMessage(msg));
    }
}
