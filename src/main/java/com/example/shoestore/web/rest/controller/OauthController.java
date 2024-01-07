package com.example.shoestore.web.rest.controller;

import com.example.shoestore.infrastructure.constants.HeaderConstants;
import com.example.shoestore.sercurity.provider.oath2.AuthService;
import com.example.shoestore.sercurity.sign.SignInForm;
import com.example.shoestore.sercurity.sign.SignInResponse;
import com.example.shoestore.sercurity.sign.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final SignService signService;
    private final AuthService authService;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody SignInForm signInForm) {
        HttpHeaders httpHeaders = new HttpHeaders();
        SignInResponse signInResponse = signService.signIn(signInForm);
        httpHeaders.add(HeaderConstants.AUTHORIZATION, "Bearer " + signInResponse.getToken());
        return new ResponseEntity<>(signInResponse, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/googleRedirectUri")
    public ResponseEntity<String> getGoogleRedirectUri() {
        return ResponseEntity.ok(googleRedirectUri);
    }

    @PostMapping("/oauth/google")
    public ResponseEntity<Object> exchangeCodeForToken(@RequestParam("code") String code) {

        HttpHeaders httpHeaders = new HttpHeaders();
        SignInResponse signInResponse = authService.signInByCode(code);
        httpHeaders.add(HeaderConstants.AUTHORIZATION, "Bearer " + signInResponse.getToken());
        return new ResponseEntity<>(signInResponse, httpHeaders, HttpStatus.OK);
    }

}
