package com.example.shoestore.core.account.client.service;

import com.example.shoestore.core.account.client.model.request.RegisterClientDTO;
import com.example.shoestore.core.account.client.model.request.UpdateClientDTO;
import com.example.shoestore.core.account.staff.model.request.ChangePasswordDTO;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ClientService {
    ResponseDTO findAccount(Long id);

    ResponseDTO createAccount(RegisterClientDTO clientDTO);

    ResponseDTO updateAccount(UpdateClientDTO clientDTO);

    ResponseDTO deleteAccount(Long id);

    ResponseDTO changeAvatar(Long id, MultipartFile file);

    ResponseDTO OTPSend(String emailOrPhonenumber);

    ResponseDTO OTPCheck(int otp);

    ResponseDTO changePassword(ChangePasswordDTO changePasswordDTO);
}
