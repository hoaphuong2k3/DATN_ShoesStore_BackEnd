package com.example.shoestore.sercurity.provider.jwt;

import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.sercurity.exception.TokenInvalidException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Value("${jwt.secret}")
    private String SECRET;

    public static final String AUTHORITIES = "Authorities";

    public String generateToken(Authentication authentication, Boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        Date expirationDate;
        if (rememberMe) {
            expirationDate = generateExpirationDateForRememberMe();
        } else {
            expirationDate = generateExpirationDate();
        }
        return Jwts
                .builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES, authorities)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .setExpiration(expirationDate)
                .compact();
    }

    public Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24)); // 1 day
    }

    public Date generateExpirationDateForRememberMe() {
        return new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 30)); // 1 month
    }


    @Override
    public Authentication authenticate(Authentication authentication) {
        String jwt = String.valueOf(authentication.getPrincipal());
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(jwt)
                .getBody();

        Collection<? extends GrantedAuthority> grantedAuthorities = Arrays
                .stream(claims.get(AUTHORITIES).toString().split(","))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        String subject = claims.getSubject();
        return new JwtAuthenticationToken(subject, jwt, grantedAuthorities);
    }

    public boolean validateToken(String token) {
        try {

            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();

            return true;
        } catch (ExpiredJwtException e) {
            throw new TokenInvalidException(MessageCode.Commom.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new TokenInvalidException(MessageCode.Commom.TOKEN_UN_SUPPORT);
        } catch (MalformedJwtException e) {
            throw new TokenInvalidException(MessageCode.Commom.TOKEN_MALFORMED);
        } catch (SignatureException e) {
            throw new TokenInvalidException(MessageCode.Commom.SIGNATURE_INVALID);
        } catch (IllegalArgumentException e) {
            throw new TokenInvalidException(MessageCode.Commom.TOKEN_ERROR);
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
