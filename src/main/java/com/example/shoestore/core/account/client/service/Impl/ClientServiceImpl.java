package com.example.shoestore.core.account.client.service.Impl;

import com.example.shoestore.core.account.client.model.request.RegisterClientDTO;
import com.example.shoestore.core.account.client.model.request.UpdateClientDTO;
import com.example.shoestore.core.account.client.model.response.ClientResponse;
import com.example.shoestore.core.account.staff.model.request.ChangePasswordDTO;
import com.example.shoestore.core.account.staff.model.response.ForgotPasswordResponse;
import com.example.shoestore.core.account.staff.repository.UserAccountRoleRepository;
import com.example.shoestore.core.account.staff.service.impl.AddressServiceImpl;
import com.example.shoestore.core.account.client.repository.UserClientRepository;
import com.example.shoestore.core.account.client.service.ClientService;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.ClientMapper;
import com.example.shoestore.entity.Client;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@Service
public class ClientServiceImpl implements ClientService {


    @Autowired
    private UserClientRepository clientRepository;

    @Autowired
    private UserAccountRoleRepository userAccountRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ClientMapper mapper;

    @Autowired
    private AddressServiceImpl addressService;
    private static Client client = new Client();

    private static int randomOTP;

    private static String emailOrPhonenumberRequest;

    @Override
    public ResponseDTO findAccount(Long id) {

        return ResponseDTO.success(clientRepository.findAccount(id));
    }

    @Override
    public ResponseDTO createAccount(RegisterClientDTO clientDTO) {

        client = mapper.registerDTOToEntity(clientDTO);
        client.setIsDeleted(IsDeleted.UNDELETED.getValue());
        client.setStatus(Status.ACTIVATE.getValue());
        client.setIdRole(RoleAccount.USER.getValue());
        client.setCreatedTime(LocalDateTime.now());
        Client clientSave = clientRepository.save(client);


        ClientResponse clientResponse = mapper.entityToDTO(clientSave);
        return ResponseDTO.success(clientResponse);

    }

    @Override
    public ResponseDTO updateAccount(UpdateClientDTO clientDTO) {
        clientDTO.setUpdateTime(LocalDateTime.now());
        clientRepository.updateAccount(clientDTO);
        return ResponseDTO.success();

    }

    @SneakyThrows
    @Override
    public ResponseDTO deleteAccount(Long id) {

        if (clientRepository.existsById(id)) {
            clientRepository.deleteClient(IsDeleted.DELETED.getValue(), id);
            return ResponseDTO.success(ResponseMess.SUCCESS);
        }
        throw new ResourceNotFoundException(MessageUtils.getMessage(MessageCode.Accounts.ACCOUNT_NOT_FOUND));
    }

    @SneakyThrows
    @Override
    public ResponseDTO changeAvatar(Long id, MultipartFile file) {
        if (clientRepository.existsById(id)) {
            clientRepository.changeAvatar(file.getBytes(), id);
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
            ForgotPasswordResponse response = clientRepository.findByPhonenumberOrEmail(emailOrPhonenumberRequest);
            return ResponseDTO.success(response);
        }
        throw new ValidateException(MessageUtils.getMessage(MessageCode.Message.MESSAGE_OTP_MISMATCH));

    }

    @SneakyThrows
    @Override
    public ResponseDTO changePassword(ChangePasswordDTO changePasswordDTO) {
        if (isValidPassword(changePasswordDTO.getNewPassword())) {
            String passwordInDB = clientRepository.findById(changePasswordDTO.getId()).get().getPassword();
            if (checkPassword(changePasswordDTO.getOldPassword(), passwordInDB)) {

                if (changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
                    clientRepository.changePassword(
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
