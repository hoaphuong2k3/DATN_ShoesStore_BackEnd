package com.example.shoestore.core.account.client.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private Long id;
    private String username;
    private String password;
    private String fullname;
    private Boolean gender;
    private LocalDate dateOfBirth;
    private String email;
    private byte[] avatar;
    private String phoneNumber;
    private String proviceCode;
    private String districtCode;
    private String communeCode;
    private String addressDetail;
    private Integer status;
    private Integer totalPoints;
    private Boolean isDeleted;

}
