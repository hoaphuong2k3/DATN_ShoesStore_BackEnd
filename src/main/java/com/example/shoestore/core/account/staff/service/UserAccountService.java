package com.example.shoestore.core.account.staff.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.account.staff.model.request.ChangePasswordDTO;
import com.example.shoestore.core.account.staff.model.request.RegisterAccountDTO;
import com.example.shoestore.core.account.staff.model.request.UpdateAccountDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserAccountService {
    ResponseDTO findAccount(Long id);
    ResponseDTO createAccount(RegisterAccountDTO singAccountDTO);

    ResponseDTO updateAccount(UpdateAccountDTO updateAccountDTO);

    ResponseDTO deleteAccount(Long id);

    ResponseDTO changeAvatar(Long id , MultipartFile file);

    ResponseDTO OTPSend(String emailOrPhonenumber);

    ResponseDTO OTPCheck(int otp);

    ResponseDTO changePassword(ChangePasswordDTO changePasswordDTO);
}
