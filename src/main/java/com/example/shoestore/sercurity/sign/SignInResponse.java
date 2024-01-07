package com.example.shoestore.sercurity.sign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignInResponse {

    private String token;

    private Long userId;

    private Collection<? extends GrantedAuthority> authorities;

}
