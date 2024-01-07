package com.example.shoestore.sercurity.provider.oath2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserGoogleResponse {

    private String email;

    private String fullName;

    private String gender;

    private LocalDate dateOfBirth;

    private String address;

    private String phoneNumber;

    private String avatar;

}
