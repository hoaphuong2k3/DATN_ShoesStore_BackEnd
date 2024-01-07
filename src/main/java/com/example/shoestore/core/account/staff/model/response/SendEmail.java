package com.example.shoestore.core.account.staff.model.response;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendEmail implements Serializable {

    private String email;
    private String userName;
    private String password;
}
