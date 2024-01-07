package com.example.shoestore.sercurity.sign;

import com.example.shoestore.infrastructure.constants.MessageCode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignInForm {

    @NotBlank(message = MessageCode.Login.USERNAME_NOT_BLANK)
    private String username;

    @NotBlank(message = MessageCode.Login.PASSWORD_NOT_BLANK)
    private String password;

    private Boolean rememberMe;

}
