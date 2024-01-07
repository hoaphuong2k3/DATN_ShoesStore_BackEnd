package com.example.shoestore.core.account.staff.service.impl;

import com.example.shoestore.core.account.staff.model.request.*;
import com.example.shoestore.core.account.staff.model.response.AccountResponse;
import com.example.shoestore.core.account.staff.model.response.AdminAccountResponse;
import com.example.shoestore.core.account.staff.model.response.ForgotPasswordResponse;
import com.example.shoestore.core.account.staff.model.response.SendEmail;
import com.example.shoestore.core.account.staff.repository.AdminMemberAccountRespository;
import com.example.shoestore.core.account.staff.repository.UserAccountRoleRepository;
import com.example.shoestore.core.account.staff.repository.AccountAddressRepository;
import com.example.shoestore.core.account.staff.service.AdminAccountService;
import com.example.shoestore.core.common.BaseService;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.AccountMapper;
import com.example.shoestore.entity.MemberAccount;
import com.example.shoestore.entity.AccountRole;
import com.example.shoestore.entity.Address;
import com.example.shoestore.infrastructure.constants.*;
import com.example.shoestore.infrastructure.exception.ResourceNotFoundException;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.DateUtils;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import com.example.shoestore.infrastructure.utils.MessageUtils;
import com.example.shoestore.sercurity.SercurityConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class AdminAccountServiceImpl extends BaseService implements AdminAccountService {

//    @Autowired
//    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    private AdminMemberAccountRespository adminAccountRespository;
    @Autowired
    private UserAccountRoleRepository userAccountRoleRepository;
    @Autowired
    private AccountAddressRepository addressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AccountMapper mapper;
    private static String emailOrPhonenumberRequest;


    @Autowired
    private AddressServiceImpl addressService;
    private static MemberAccount memberAccount = new MemberAccount();

    private static int randomOTP;


    @SneakyThrows
    @Override
    public ResponseDTO createAdmin(CreateAdminDTO accountDTO) {
        String password = GenerateCode.RandomStringGenerator();
        Long idAddress = this.getIdAddress(accountDTO.getAddress());
        memberAccount = mapper.adminDTOToEntity(accountDTO);
        memberAccount.setDateOfBirth(DateUtils.strToLocalDate(accountDTO.getDateOfBirth()));
        memberAccount.setPassword(SercurityConfig.encoder().encode(password));
        memberAccount.setIsDeleted(IsDeleted.UNDELETED.getValue());
        memberAccount.setCreatedTime(LocalDateTime.now());
        memberAccount.setStatus(Status.ACTIVATE.getValue());
        memberAccount.setIdAddress(idAddress);
        MemberAccount saveMemberAccount = adminAccountRespository.save(memberAccount);
        Long idAccount = saveMemberAccount.getId();
        addRole(idAccount, RoleAccount.STAFF.getValue());
        AccountResponse accountResponse = mapper.entityToDTO(saveMemberAccount);
        SendEmail sendEmail = mapper.sendEmailEntityToDTO(saveMemberAccount, password);
        this.sendToEmail(sendEmail);

        return ResponseDTO.success(accountResponse);
    }

    @Override
    public ResponseDTO createSuperAdmin() {
        this.createAdmin();
        this.createStaff();
        return ResponseDTO.success();
    }

    private void createAdmin() {
        memberAccount.setUsername("admin");
        memberAccount.setFullname("ADMIN");
        memberAccount.setPassword(SercurityConfig.encoder().encode("123"));
        memberAccount.setIsDeleted(IsDeleted.UNDELETED.getValue());
        memberAccount.setPhoneNumber("0354875641");
        memberAccount.setEmail("admin@gmail.com");
        memberAccount.setStatus(Status.ACTIVATE.getValue());
        MemberAccount saveMemberAccount = adminAccountRespository.save(memberAccount);
        Long idAccount = saveMemberAccount.getId();
        this.addRole(idAccount, RoleAccount.ADMIN.getValue());
        this.addRole(idAccount, RoleAccount.STAFF.getValue());
    }

    private void createStaff() {

        MemberAccount staffAccount = new MemberAccount();

        staffAccount.setUsername("staff");
        staffAccount.setFullname("STAFF");
        staffAccount.setPassword(SercurityConfig.encoder().encode("123"));
        staffAccount.setIsDeleted(IsDeleted.UNDELETED.getValue());
        staffAccount.setPhoneNumber("0354875641");
        staffAccount.setEmail("staff@gmail.com");
        staffAccount.setStatus(Status.ACTIVATE.getValue());
        MemberAccount saveMemberAccount = adminAccountRespository.save(staffAccount);
        Long idAccount = saveMemberAccount.getId();
        this.addRole(idAccount, RoleAccount.STAFF.getValue());
    }

    @Override
    public ResponseDTO adminPage(String fullname, String email, String phonenumber, Boolean gender, LocalDateTime createdTime, LocalDateTime updatedTime, int page, int size, String sortField, String sortOrder) {
        PageRequest pageRequest = BaseService.createPageRequest(page, size, sortField, sortOrder);
        Page<AdminAccountResponse> accountResponsePage = adminAccountRespository.findAdminAccount(fullname, IsDeleted.UNDELETED.getValue(), email, phonenumber, gender, createdTime, updatedTime, pageRequest);
        return ResponseDTO.success(accountResponsePage);
    }


    @Override
    public ResponseDTO findAdmin(Long id) {

        AdminAccountResponse adminAccountResponse = adminAccountRespository.findAccount(id);

        return ResponseDTO.success(adminAccountResponse);

    }


    @Override
    public ResponseDTO updateAdmin(UpdateAccountDTO updateAccountDTO) {
        Long idAddress = addressService.getIdAddress(updateAccountDTO.getAddress());
        memberAccount = mapper.updateDTOToEntity(updateAccountDTO);
        memberAccount.setIdAddress(idAddress);
        memberAccount.setUpdatedTime(LocalDateTime.now());
        adminAccountRespository.updateAccount(memberAccount);
        return ResponseDTO.success();
    }

    @Override
    public ResponseDTO deleteAdmin(DeleteAccountDTO deleteAccountDTO) {
            adminAccountRespository.deleteAccount(IsDeleted.DELETED.getValue(), deleteAccountDTO.getId());
            return ResponseDTO.success(ResponseMess.SUCCESS);
    }

    @SneakyThrows
    @Override
    public ResponseDTO changeAvatar(Long id, MultipartFile file) {
        if (adminAccountRespository.existsById(id)) {
            adminAccountRespository.changeAvatar(file.getBytes(), id);
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
            ForgotPasswordResponse response = adminAccountRespository.findByPhonenumberOrEmail(emailOrPhonenumberRequest).orElseThrow(()
                    -> new ResourceNotFoundException(MessageUtils.getMessage(MessageCode.Accounts.ACCOUNT_NOT_FOUND)));
            return ResponseDTO.success(response);
        }
        throw new ValidateException(MessageUtils.getMessage(MessageCode.Message.MESSAGE_OTP_MISMATCH));

    }

    @SneakyThrows
    @Override
    public ResponseDTO changePassword(ChangePasswordDTO changePasswordDTO) {
        String passwordInDB = adminAccountRespository.findById(changePasswordDTO.getId()).get().getPassword();
        if (checkPassword(changePasswordDTO.getOldPassword(), passwordInDB)) {

            if (changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
                adminAccountRespository.changePassword(
                        passwordEncoder.encode(changePasswordDTO.getNewPassword()),
                        changePasswordDTO.getId());
                return ResponseDTO.success(MessageUtils.getMessage(MessageCode.Accounts.CHANGE_PASSWORD_SUCCESS));
            }
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Message.MESSAGE_PASSWORD_MISMATCH));
        }
        throw new ValidateException(MessageUtils.getMessage(MessageCode.Message.MESSAGE_PASSWORD_WRONG));

    }


    @SneakyThrows
    @Override
    public ResponseDTO forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
        if (isValidPassword(forgotPasswordDTO.getNewPassword())) {
            Long id = adminAccountRespository.findIdStaff(emailOrPhonenumberRequest);
            if (forgotPasswordDTO.getNewPassword().equals(forgotPasswordDTO.getConfirmPassword())) {
                adminAccountRespository.changePassword(
                        passwordEncoder.encode(forgotPasswordDTO.getNewPassword()), id);
                return ResponseDTO.success(MessageUtils.getMessage(MessageCode.Accounts.CHANGE_PASSWORD_SUCCESS));
            }

            throw new ValidateException(MessageUtils.getMessage(MessageCode.Message.MESSAGE_PASSWORD_MISMATCH));
        }
        return ResponseDTO.success();
    }

    private boolean checkPassword(String oldPassword, String passwordInDB) {
        if (passwordEncoder.matches(oldPassword, passwordInDB)) {
            return true;
        }
        return false;
    }

    @Override
    public ResponseDTO updateStatus(Integer status, Long id) {
        if (adminAccountRespository.existsById(id)) {
            adminAccountRespository.updateStatus(status, id);
            return ResponseDTO.success();
        }
        return ResponseDTO.success("Id Không tồn tại !");
    }

    private void addRole(Long idAccount, Long idRole) {
        AccountRole accountRole = new AccountRole();
        accountRole.setIdAccount(idAccount);
        accountRole.setIdRole(idRole);
        userAccountRoleRepository.save(accountRole);
    }

    private Long getIdAddress(CreateAddressDTO addressDTO) {
        Address address = new Address();
        address.setProviceCode(addressDTO.getProviceCode());
        address.setDistrictCode(addressDTO.getDistrictCode());
        address.setCommuneCode(addressDTO.getCommuneCode());
        address.setAddressDetail(addressDTO.getAddressDetail());
        address.setIsDeleted(IsDeleted.UNDELETED.getValue());

        return addressRepository.save(address).getId();
    }

//    @RabbitListener(queues = {RabbitMQConfig.TOPIC_QUEUE_STAFF})
    private void sendToEmail(SendEmail sendEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(MessageCode.Email.EMAIL_FROM);
        message.setTo(sendEmail.getEmail());
        message.setText(MessageCode.Email.EMAIL_TEXT + "\n" +
                MessageCode.Email.EMAIL_USERNAME + sendEmail.getUserName() + "\n" +
                MessageCode.Email.EMAIL_PASSWORD + sendEmail.getPassword());
        message.setSubject(MessageCode.Email.EMAIL_SUBJECT_CONFIRM_ADMIN);
        mailSender.send(message);
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
