package com.example.shoestore.sercurity.provider.oath2;

import com.example.shoestore.core.common.mapper.ClientMapper;
import com.example.shoestore.entity.Client;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.DataUtils;
import com.example.shoestore.infrastructure.utils.MessageUtils;
import com.example.shoestore.sercurity.exception.UserDetailNotfoundException;
import com.example.shoestore.sercurity.provider.repository.SecurityClientRepository;
import com.example.shoestore.sercurity.provider.repository.SecurityRoleRepository;
import com.example.shoestore.sercurity.provider.jwt.JwtAuthenticationProvider;
import com.example.shoestore.sercurity.sign.SignInResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
@RequiredArgsConstructor

public class GoogleAuthService implements AuthService{

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String scope;

    @Value("${google.api.token.info.url}")
    private String uriGetUser;

    @Value("${google.api.people.url}")
    private String uriGetUserInfo;

    @Value("${google.api.token.url}")
    private String uriToken;

    private final RestTemplate restTemplate;

    private final ClientMapper clientMapper;

    private final SecurityClientRepository clientRepository;

    private final SecurityRoleRepository roleRepository;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private TokenGoogleResponse getTokenByCode(String code) {

        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("code", code);
        requestMap.add("client_id", clientId);
        requestMap.add("client_secret", clientSecret);
        requestMap.add("redirect_uri", redirectUri);
        requestMap.add("grant_type", "authorization_code");

        Map<String, Object> response = restTemplate.postForObject(uriToken, requestMap, Map.class);

        TokenGoogleResponse token = new TokenGoogleResponse();
        token.setIdToken(String.valueOf(response.get("id_token")));
        token.setAccessToken(String.valueOf(response.get("access_token").toString()));

        return token;
    }

    @SneakyThrows
    public UserGoogleResponse getUserInfoByToken(String idToken,String accessToken) {

        String apiUserEndpoint = uriGetUser + "?id_token=" + idToken;

        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();

        Map<String, Object> response = restTemplate.postForObject(apiUserEndpoint, requestMap,Map.class);

        UserGoogleResponse userGoogleResponse = new UserGoogleResponse();

        userGoogleResponse.setEmail(String.valueOf(response.get("email")));
        userGoogleResponse.setFullName(String.valueOf(response.get("name")));
        userGoogleResponse.setAvatar(String.valueOf(response.get("picture")));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        String apiUserInfoEndpoint = uriGetUserInfo + "/me?personFields=addresses,genders,birthdays,phoneNumbers";

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                apiUserInfoEndpoint,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> userInfo = responseEntity.getBody();

            if (userInfo != null) {
                this.handleGender(userInfo,userGoogleResponse);
                this.handleDateOfBirth(userInfo,userGoogleResponse);
                this.handleAddress(userInfo,userGoogleResponse);
                this.handlePhoneNumbers(userInfo,userGoogleResponse);
            }

        }

        return userGoogleResponse;
    }

    private void handleGender(Map<String, Object> userInfo, UserGoogleResponse userGoogleResponse) {
        if (userInfo.containsKey("genders")) {
            List<Map<String, String>> gendersList = (List<Map<String, String>>) userInfo.get("genders");
            for (Map<String, String> genderMap : gendersList) {
                String gender = genderMap.get("formattedValue");
                if (gender != null) {
                    userGoogleResponse.setGender(gender);
                    break;
                }
            }
        }
    }

    private void handleDateOfBirth(Map<String, Object> userInfo, UserGoogleResponse userGoogleResponse) {
        if (userInfo.containsKey("birthdays")) {
            List<Map<String, Object>> birthdaysList = (List<Map<String, Object>>) userInfo.get("birthdays");
            for (Map<String, Object> birthdayMap : birthdaysList) {
                Map<String, Integer> dateInfo = (Map<String, Integer>) birthdayMap.get("date");
                if (dateInfo != null) {
                    Integer year = dateInfo.get("year");
                    Integer month = dateInfo.get("month");
                    Integer day = dateInfo.get("day");
                    if (year != null && month != null && day != null) {
                        userGoogleResponse.setDateOfBirth(LocalDate.of(year, month, day));
                        break;
                    }
                }
            }
        }
    }

    private void handleAddress(Map<String, Object> userInfo, UserGoogleResponse userGoogleResponse) {
        if (userInfo.containsKey("addresses")) {
            List<Map<String, String>> addressesList = (List<Map<String, String>>) userInfo.get("addresses");
            for (Map<String, String> addressMap : addressesList) {
                String formattedValue = addressMap.get("formattedValue");
                if (formattedValue != null) {
                    userGoogleResponse.setAddress(formattedValue);
                    break;
                }
            }
        }
    }

    private void handlePhoneNumbers(Map<String, Object> userInfo, UserGoogleResponse userGoogleResponse) {
        if (userInfo.containsKey("phoneNumbers")) {
            List<Map<String, Object>> phoneNumbersList = (List<Map<String, Object>>) userInfo.get("phoneNumbers");
            for (Map<String, Object> phoneNumberMap : phoneNumbersList) {
                String phoneNumber = (String) phoneNumberMap.get("value");
                if (phoneNumber != null) {
                    String phoneNumberFormat = this.normalizePhoneNumber(phoneNumber);
                    userGoogleResponse.setPhoneNumber(phoneNumberFormat);
                    break;
                }
            }
        }
    }

    private String normalizePhoneNumber(String originalPhoneNumber) {
        String digitsOnly = originalPhoneNumber.replaceAll("[^0-9]", "");

        digitsOnly = "0" + digitsOnly;

        return digitsOnly;
    }


    @SneakyThrows
    public SignInResponse signInByCode(String code) {

        TokenGoogleResponse tokenGoogleResponse = this.getTokenByCode(code);

        String idToken = tokenGoogleResponse.getIdToken();

        UserGoogleResponse userGoogle = this.getUserInfoByToken(idToken,tokenGoogleResponse.getAccessToken());

        String email = userGoogle.getEmail();

        Client clientByEmail = clientRepository.getClientByEmail(email);

        if(DataUtils.isNull(clientByEmail)){
            Client client = clientMapper.userGoogleToEntity(userGoogle);
            client = clientRepository.save(client);
        }

        Client client = clientRepository.getClientByEmail(email);

        if(DataUtils.isNotNull(client)){
            if(DataUtils.isNotNull(client.getPassword())||DataUtils.isNotNull(client.getUsername())){
                throw new ValidateException(MessageCode.Accounts.EMAIL_EXISTS);
            }
        }

        String roleClient = roleRepository.getRoleNameByRoleId(client.getIdRole()).orElseThrow(()
                -> new UserDetailNotfoundException(MessageUtils.getMessage(MessageCode.Accounts.ROLES_NOT_FOUND)));

        Set<SimpleGrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(roleClient));

        String token =  jwtAuthenticationProvider.generateToken(new OAuth2AuthenticationToken(client.getEmail(),idToken,authorities),Boolean.FALSE);

        SignInResponse signInResponse = new SignInResponse();
        signInResponse.setToken(token);
        signInResponse.setUserId(client.getId());
        signInResponse.setAuthorities(authorities);

        return signInResponse;
    }

}


