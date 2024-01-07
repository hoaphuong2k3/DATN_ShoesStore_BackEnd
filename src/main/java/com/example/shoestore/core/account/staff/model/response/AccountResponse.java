package com.example.shoestore.core.account.staff.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountResponse {
    private Long id;
    private String userName;
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
    private Boolean isDeleted;

}
