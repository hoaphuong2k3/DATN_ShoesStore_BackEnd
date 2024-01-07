package com.example.shoestore.core.account.staff.service.impl;

import com.example.shoestore.core.account.staff.repository.UserMemberAccountRepository;
import com.example.shoestore.core.account.staff.repository.UserAccountRoleRepository;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.AccountMapper;
import com.example.shoestore.core.account.staff.model.request.ChangePasswordDTO;
import com.example.shoestore.core.account.staff.model.request.RegisterAccountDTO;
import com.example.shoestore.core.account.staff.model.request.UpdateAccountDTO;
import com.example.shoestore.core.account.staff.model.response.AccountResponse;
import com.example.shoestore.core.account.staff.model.response.ForgotPasswordResponse;
import com.example.shoestore.core.account.staff.service.UserAccountService;
import com.example.shoestore.entity.MemberAccount;
import com.example.shoestore.entity.AccountRole;
import com.example.shoestore.infrastructure.constants.*;
import com.example.shoestore.infrastructure.exception.ResourceNotFoundException;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import com.example.shoestore.infrastructure.utils.MessageUtils;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.regex.Pattern;


@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserMemberAccountRepository userAccountRepository;

    @Autowired
    private UserAccountRoleRepository userAccountRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AccountMapper mapper;

    @Autowired
    private AddressServiceImpl addressService;
    private static MemberAccount memberAccount = new MemberAccount();

    private static int randomOTP;

    private static String emailOrPhonenumberRequest;

    @Override
    public ResponseDTO findAccount(Long id) {

        return ResponseDTO.success(userAccountRepository.findAccount(id));
    }

    @Override
    public ResponseDTO createAccount(RegisterAccountDTO accountDTO) {

        memberAccount = mapper.registerDTOToEntity(accountDTO);
        memberAccount.setIsDeleted(IsDeleted.UNDELETED.getValue());
        memberAccount.setStatus(Status.ACTIVATE.getValue());
        memberAccount.setCreatedTime(LocalDateTime.now());

        MemberAccount saveMemberAccount = userAccountRepository.save(memberAccount);

        Long idAccount = saveMemberAccount.getId();
        addRoleUser(idAccount);

        AccountResponse accountResponse = mapper.entityToDTO(saveMemberAccount);
        return ResponseDTO.success(accountResponse);

    }

    @Override
    public ResponseDTO updateAccount(UpdateAccountDTO accountDTO) {
        Long idAddress = addressService.getIdAddress(accountDTO.getAddress());
        memberAccount = mapper.updateDTOToEntity(accountDTO);
        memberAccount.setIdAddress(idAddress);
        memberAccount.setUpdatedTime(LocalDateTime.now());
        userAccountRepository.updateAccount(memberAccount);
        return ResponseDTO.success();

    }

    @SneakyThrows
    @Override
    public ResponseDTO deleteAccount(Long id) {

        if (userAccountRepository.existsById(id)) {
            memberAccount.setIsDeleted(IsDeleted.DELETED.getValue());
            userAccountRepository.save(memberAccount);
            return ResponseDTO.success(ResponseMess.SUCCESS);
        }
        throw new ResourceNotFoundException(MessageUtils.getMessage(MessageCode.Accounts.ACCOUNT_NOT_FOUND));
    }

    @SneakyThrows
    @Override
    public ResponseDTO changeAvatar(Long id, MultipartFile file) {
        if (userAccountRepository.existsById(id)) {
            userAccountRepository.changeAvatar(file.getBytes(), id);
            return ResponseDTO.success(ResponseMess.SUCCESS);

        }
        throw new ResourceNotFoundException(MessageUtils.getMessage(MessageCode.Accounts.ACCOUNT_NOT_FOUND));

    }

    @SneakyThrows
    @Override
    public ResponseDTO OTPSend(String emailOrPhonenumber) {
        randomOTP = GenerateCode.code();
        emailOrPhonenumberRequest = emailOrPhonenumber;

        if (emailOrPhonenumber.matches(RegexPattern.REGEX_PHONENUMBER)) {
            sendToPhone(emailOrPhonenumber, randomOTP);
        } else if (emailOrPhonenumber.matches(RegexPattern.REGEX_EMAIL)) {
            sendToEmail(emailOrPhonenumber, randomOTP);
        } else {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Message.MESSAGE_ERROR_PHONENUMBER_OR_EMAIL));
        }

        return ResponseDTO.success();
    }

    @SneakyThrows
    @Override
    public ResponseDTO OTPCheck(int otp) {
        if (otp == randomOTP) {
            ForgotPasswordResponse response = userAccountRepository.findByPhonenumberOrEmail(emailOrPhonenumberRequest).orElseThrow(()
                    -> new ResourceNotFoundException(MessageUtils.getMessage(MessageCode.Accounts.ACCOUNT_NOT_FOUND)));
            return ResponseDTO.success(response);
        }
        throw new ValidateException(MessageUtils.getMessage(MessageCode.Message.MESSAGE_OTP_MISMATCH));

    }

    @SneakyThrows
    @Override
    public ResponseDTO changePassword(ChangePasswordDTO changePasswordDTO) {
        if (isValidPassword(changePasswordDTO.getNewPassword())) {
            String passwordInDB = userAccountRepository.findById(changePasswordDTO.getId()).get().getPassword();
            if (checkPassword(changePasswordDTO.getOldPassword(), passwordInDB)) {

                if (changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
                    userAccountRepository.changePassword(
                            passwordEncoder.encode(changePasswordDTO.getNewPassword()),
                            changePasswordDTO.getId());
                    return ResponseDTO.success(MessageUtils.getMessage(MessageCode.Accounts.CHANGE_PASSWORD_SUCCESS));
                }
                throw new ValidateException(MessageUtils.getMessage(MessageCode.Message.MESSAGE_PASSWORD_MISMATCH));
            }

        }
        throw new ValidateException(MessageUtils.getMessage(MessageCode.Message.MESSAGE_PASSWORD_WRONG));

    }

    private boolean checkPassword(String oldPassword, String passwordInDB) {
        if (passwordEncoder.matches(oldPassword, passwordInDB)) {
            return true;
        }
        return false;
    }

    private void sendToPhone(String phonenumber, int otp) {
        String convertPhonenumber = convertToInternationalFormat(phonenumber);
        Twilio.init(TwilioToken.ACCOUNT_SID, TwilioToken.AUTH_TOKEN);
        Message.creator(
                new PhoneNumber(convertPhonenumber),
                new PhoneNumber(TwilioToken.PHONENUMBER),
                MessageCode.Message.MESSAGE_OTP + otp
        ).create();

    }

    private void sendToEmail(String email, int otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(MessageCode.Email.EMAIL_FROM);
        message.setTo(email);
        message.setText(MessageCode.Message.MESSAGE_EMAIL + otp);
        message.setSubject(MessageCode.Email.EMAIL_SUBJECT_CONFIRM);
        mailSender.send(message);
    }


    private void addRoleUser(Long idAccount) {
        AccountRole accountRole = new AccountRole();
        accountRole.setIdAccount(idAccount);
        accountRole.setIdRole(RoleAccount.USER.getValue());
        userAccountRoleRepository.save(accountRole);
    }

    @SneakyThrows
    private boolean isValidPassword(String password) {
        if (!password.matches(RegexPattern.REGEX_UPPERCASE_CHARACTER)) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Accounts.PASSWORD_UPPERCASE));
        }
        if (!password.matches(RegexPattern.REGEX_LOWERCASE_CHARACTER)) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Accounts.PASSWORD_LOWERCASE));
        }
        if (!password.matches(RegexPattern.REGEX_NUMBER)) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Accounts.PASSWORD_NUMBER));
        }
        Pattern specialCharPattern = Pattern.compile(RegexPattern.REGEX_SPECIAL_CHARACTER);
        if (!specialCharPattern.matcher(password).find()) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Accounts.PASSWORD_SPECIAL));
        }
        return true;

    }

    private String convertToInternationalFormat(String phonenumber) {
        if (phonenumber.startsWith("0")) {
            return "+84" + phonenumber.substring(1);
        }
        return "+84" + phonenumber;
    }

}
