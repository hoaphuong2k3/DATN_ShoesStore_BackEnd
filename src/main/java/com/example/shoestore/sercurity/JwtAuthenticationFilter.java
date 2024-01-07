package com.example.shoestore.sercurity;

import com.example.shoestore.infrastructure.constants.HeaderConstants;
import com.example.shoestore.infrastructure.utils.DataUtils;
import com.example.shoestore.sercurity.provider.jwt.JwtAuthenticationProvider;
import com.example.shoestore.sercurity.provider.jwt.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Locale;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtAuthenticationProvider jwtProvider;

    public JwtAuthenticationFilter(JwtAuthenticationProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request);
        if (DataUtils.isNotNull(jwt) && jwtProvider.validateToken(jwt)) {
            Authentication authentication = jwtProvider.authenticate(new JwtAuthenticationToken(jwt));
            JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authentication.getPrincipal(),jwt,authentication.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        String languageHeader = request.getHeader(HeaderConstants.ACCEPT_LANGUAGE);

        if (DataUtils.isNotNull(languageHeader)) {
            Locale locale = Locale.forLanguageTag(languageHeader);
            LocaleContextHolder.setLocale(locale);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HeaderConstants.AUTHORIZATION);
        if (DataUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}