package com.example.shoestore.core.account.staff.model.response;

import com.example.shoestore.entity.MemberAccount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = {MemberAccount.class})
public interface ForgotPasswordResponse {
    @Value("#{target.username}")
    String getUserName();

    @Value("#{target.fullname}")
    String getFullname();
}
