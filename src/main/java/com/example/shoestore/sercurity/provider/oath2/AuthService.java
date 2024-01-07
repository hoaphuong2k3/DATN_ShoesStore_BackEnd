package com.example.shoestore.sercurity.provider.oath2;

import com.example.shoestore.sercurity.sign.SignInResponse;

public interface AuthService {

    SignInResponse signInByCode(String code);
}
