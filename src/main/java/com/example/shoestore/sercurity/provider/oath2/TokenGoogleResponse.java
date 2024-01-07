package com.example.shoestore.sercurity.provider.oath2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class TokenGoogleResponse {

    private String idToken;
    private String accessToken;
}
