package com.example.shoestore.core.account.client.model.request;

import com.example.shoestore.infrastructure.constants.MessageCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientDTO {

    @NotBlank(message = MessageCode.Accounts.USERNAME_NOT_NULL)
    @Size(max = 20, min = 5, message = MessageCode.Accounts.USERNAME_MAX_MIN)
    private String username;

    private Boolean gender;

    private String dateOfBirth;
    @NotBlank(message = MessageCode.Accounts.FULL_NAME_NOT_NULL)
    @Length(max = 250, message = MessageCode.Accounts.FULLNAME_LENGTH)
    private String fullname;
    @NotBlank(message = MessageCode.Accounts.EMAIL_NOT_NULL)
    @Length(max = 250, message = MessageCode.Accounts.EMAIL_LENGTH)
    private String email;
    @NotBlank(message = MessageCode.Accounts.PHONENUMBER_NOT_NULL)
    @Length(max = 10, message = MessageCode.Accounts.PHONENUMBER_LENGTH)
    private String phonenumber;


}
