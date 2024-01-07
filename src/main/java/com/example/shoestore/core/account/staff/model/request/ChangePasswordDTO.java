package com.example.shoestore.core.account.staff.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChangePasswordDTO implements Serializable {
    private Long id;
    private String oldPassword;
    private String newPassword;

    private String confirmPassword;
}
