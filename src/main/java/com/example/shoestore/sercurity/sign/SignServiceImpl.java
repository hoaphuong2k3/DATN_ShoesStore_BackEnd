package com.example.shoestore.sercurity.sign;

import com.example.shoestore.infrastructure.utils.DataFormatUtils;
import com.example.shoestore.infrastructure.utils.DataUtils;
import com.example.shoestore.sercurity.provider.dao.DaoAuthenticationProviderCustom;
import com.example.shoestore.sercurity.provider.dao.UserDetailsCustom;
import com.example.shoestore.sercurity.provider.jwt.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {

    private final DaoAuthenticationProviderCustom daoAuthenticationProviderCustom;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    @SneakyThrows
    @Override
    public SignInResponse signIn(SignInForm signInForm) {

        DataFormatUtils.trimStringFields(signInForm);

        String username = signInForm.getUsername();
        String password = signInForm.getPassword();

        Boolean rememberMe = (DataUtils.isNotNull(signInForm.getRememberMe()) && !signInForm.getRememberMe()) ? Boolean.FALSE : Boolean.TRUE;
        Authentication authentication = daoAuthenticationProviderCustom.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserDetailsCustom userDetails = (UserDetailsCustom) authentication.getPrincipal();
        String token = jwtAuthenticationProvider.generateToken(authentication,rememberMe);
        SignInResponse signInResponse = new SignInResponse();
        signInResponse.setToken(token);
        signInResponse.setUserId(userDetails.getUserId());
        signInResponse.setAuthorities(authentication.getAuthorities());
        return signInResponse;

    }

}
