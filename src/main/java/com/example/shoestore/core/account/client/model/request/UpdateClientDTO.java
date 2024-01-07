package com.example.shoestore.core.account.client.model.request;

import com.example.shoestore.core.account.staff.model.request.CreateAddressDTO;
import com.example.shoestore.infrastructure.constants.MessageCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClientDTO {

    private Long id;

    @Length(max = 250, message = MessageCode.Accounts.FULLNAME_LENGTH)
    private String fullname;

    private Boolean gender;

    private String dateOfBirth;

    @Length(max = 250, message = MessageCode.Accounts.EMAIL_LENGTH)
    private String email;

    @Length(max = 10, message = MessageCode.Accounts.PHONENUMBER_LENGTH)
    private String phoneNumber;
    private LocalDateTime updateTime;

    private CreateAddressDTO address;

}
