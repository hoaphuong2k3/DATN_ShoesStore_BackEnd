package com.example.shoestore.core.account.staff.service;


import com.example.shoestore.core.account.staff.model.request.*;
import com.example.shoestore.core.account.staff.model.response.AdminAccountResponse;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminAccountService {

    ResponseDTO createAdmin(CreateAdminDTO adminDTO);
    ResponseDTO createSuperAdmin();

    ResponseDTO adminPage(String fullname, String email, String phonenumber, Boolean gender, LocalDateTime createdTime, LocalDateTime updatedTime, int page, int size, String sortField, String sortOrder);

    ResponseDTO findAdmin(Long id);

    ResponseDTO updateAdmin(UpdateAccountDTO updateAccountDTO);

    ResponseDTO deleteAdmin(DeleteAccountDTO deleteAccountDTO);

    ResponseDTO changeAvatar(Long id, MultipartFile file);

    ResponseDTO OTPSend(String emailOrPhonenumber);

    ResponseDTO OTPCheck(int otp);

    ResponseDTO forgotPassword(ForgotPasswordDTO forgotPasswordDTO);

    ResponseDTO changePassword(ChangePasswordDTO changePasswordDTO);

    ResponseDTO updateStatus(Integer status, Long id);
}
