package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.account.staff.model.request.CreateAdminDTO;
import com.example.shoestore.core.account.staff.model.response.SendEmail;
import com.example.shoestore.core.common.mapper.AccountMapper;
import com.example.shoestore.core.account.staff.model.request.RegisterAccountDTO;
import com.example.shoestore.core.account.staff.model.request.UpdateAccountDTO;
import com.example.shoestore.core.account.staff.model.response.AccountResponse;
import com.example.shoestore.core.account.staff.repository.UserMemberAccountRepository;
import com.example.shoestore.entity.Client;
import com.example.shoestore.entity.MemberAccount;
import com.example.shoestore.entity.Address;
import com.example.shoestore.infrastructure.constants.IsDeleted;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.constants.RegexPattern;
import com.example.shoestore.infrastructure.exception.ResourceNotFoundException;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.DataFormatUtils;
import com.example.shoestore.infrastructure.utils.DataUtils;
import com.example.shoestore.infrastructure.utils.DateUtils;
import com.example.shoestore.infrastructure.utils.MessageUtils;
import com.example.shoestore.repository.AddressRepository;
import com.example.shoestore.sercurity.SercurityConfig;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AccountMapperImpl implements AccountMapper {

    @Autowired
    private UserMemberAccountRepository userAccountRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public MemberAccount DTOToEntity(AccountResponse dto) {
        return null;
    }

    @Override
    public AccountResponse entityToDTO(MemberAccount entity) {
        if (entity == null) {
            return null;
        }


        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(entity.getId());
        accountResponse.setUserName(entity.getUsername());
        accountResponse.setPassword(entity.getPassword());
        accountResponse.setFullname(entity.getFullname());
        accountResponse.setGender(entity.getGender());
        accountResponse.setDateOfBirth(entity.getDateOfBirth());
        accountResponse.setEmail(entity.getEmail());
        accountResponse.setAvatar(entity.getAvatar());
        accountResponse.setPhoneNumber(entity.getPhoneNumber());
        accountResponse.setIsDeleted(entity.getIsDeleted());
        if (entity.getIdAddress() != null) {
            Address address = addressRepository.findById(entity.getIdAddress()).orElse(null);
            accountResponse.setDistrictCode(address.getDistrictCode());
            accountResponse.setProviceCode(address.getProviceCode());
            accountResponse.setAddressDetail(address.getAddressDetail());
            accountResponse.setCommuneCode(address.getCommuneCode());
        }
        return accountResponse;
    }

    @SneakyThrows
    @Override
    public MemberAccount registerDTOToEntity(RegisterAccountDTO accountDTO) {
        MemberAccount memberAccount = new MemberAccount();
        if (accountDTO.getPassword().equals(accountDTO.getConfirmPassword())) {
            memberAccount.setFullname(DataFormatUtils.formatString(accountDTO.getFullname()));
            if (isValidUsername(accountDTO.getUsername())) {
                memberAccount.setUsername(DataFormatUtils.formatString(accountDTO.getUsername()));
            }
            if (isValidPassword(accountDTO.getPassword())) {
                memberAccount.setPassword(SercurityConfig.encoder().encode(DataFormatUtils.formatString(accountDTO.getPassword())));
            }
            if (isValidEmail(accountDTO.getEmail())) {
                memberAccount.setEmail(DataFormatUtils.formatString(accountDTO.getEmail()));
            }
            if (isValidPhoneNumber(accountDTO.getPhoneNumber())) {
                memberAccount.setPhoneNumber(DataFormatUtils.formatString(accountDTO.getPhoneNumber()));
            }
            return memberAccount;
        }
        throw new ValidateException(MessageUtils.getMessage(MessageCode.Message.MESSAGE_PASSWORD_MISMATCH));

    }

    @SneakyThrows
    @Override
    public MemberAccount updateDTOToEntity(UpdateAccountDTO accountDTO) {
        if (accountDTO == null || accountDTO.getId() == null) {
            return null;
        }
        Optional<MemberAccount> optionalAccount = userAccountRepository.findById(accountDTO.getId());

        if (optionalAccount.isPresent()) {
            MemberAccount memberAccount = optionalAccount.get();
            memberAccount.setId(accountDTO.getId());
            memberAccount.setFullname(DataFormatUtils.formatString(accountDTO.getFullname()));
            memberAccount.setGender(accountDTO.getGender());
            memberAccount.setDateOfBirth(DateUtils.strToLocalDate(accountDTO.getDateOfBirth()));
            if (accountDTO.getEmail().equals(memberAccount.getEmail())) {
                memberAccount.setEmail(DataFormatUtils.formatString(accountDTO.getEmail()));
            } else if (isValidEmail(accountDTO.getEmail())) {
                memberAccount.setEmail(DataFormatUtils.formatString(accountDTO.getEmail()));
            }

            if(DataUtils.isNotNull(accountDTO.getPhoneNumber())){
                if (accountDTO.getPhoneNumber().equals(memberAccount.getPhoneNumber())) {
                    memberAccount.setPhoneNumber(DataFormatUtils.formatString(accountDTO.getPhoneNumber()));
                } else if (isValidPhoneNumber(accountDTO.getPhoneNumber())) {
                    memberAccount.setPhoneNumber(DataFormatUtils.formatString(accountDTO.getPhoneNumber()));
                }
            }

            return memberAccount;
        }
        throw new ResourceNotFoundException(MessageUtils.getMessage(MessageCode.Accounts.ACCOUNT_NOT_FOUND));

    }

    @SneakyThrows
    @Override
    public MemberAccount adminDTOToEntity(CreateAdminDTO adminDTO) {
        if (adminDTO == null) {
            return null;
        }
        MemberAccount memberAccount = new MemberAccount();
            if (isValidUsername(adminDTO.getUsername())) {
                memberAccount.setUsername(DataFormatUtils.formatString(adminDTO.getUsername()));
            }
            memberAccount.setGender(adminDTO.getGender());
            memberAccount.setFullname(DataFormatUtils.formatString(adminDTO.getFullname()));
            if (adminDTO.getEmail().equals(memberAccount.getEmail())) {
                memberAccount.setEmail(DataFormatUtils.formatString(adminDTO.getEmail()));
            } else if (isValidEmail(adminDTO.getEmail())) {
                memberAccount.setEmail(DataFormatUtils.formatString(adminDTO.getEmail()));
            }
            if (adminDTO.getPhoneNumber().equals(memberAccount.getPhoneNumber())) {
                memberAccount.setPhoneNumber(DataFormatUtils.formatString(adminDTO.getPhoneNumber()));
            } else if (isValidPhoneNumber(adminDTO.getPhoneNumber())) {
                memberAccount.setPhoneNumber(DataFormatUtils.formatString(adminDTO.getPhoneNumber()));
            }

            return memberAccount;
    }

    @Override
    public SendEmail sendEmailEntityToDTO(MemberAccount memberAccount, String password) {
        if (memberAccount == null) {
            return null;
        }


        SendEmail sendEmail = new SendEmail();
        sendEmail.setEmail(memberAccount.getEmail());
        sendEmail.setUserName(memberAccount.getUsername());
        sendEmail.setPassword(password);
        return sendEmail;
    }



    @Override
    public SendEmail sendEmailClientEntityToDTO(Client memberAccount, String password) {
        if (memberAccount == null) {
            return null;
        }


        SendEmail sendEmail = new SendEmail();
        sendEmail.setEmail(memberAccount.getEmail());
        sendEmail.setUserName(memberAccount.getUsername());
        sendEmail.setPassword(password);
        return sendEmail;
    }


    @Override
    public Page<AccountResponse> pageEntityTODTO(Page<MemberAccount> pageAccount) {
        if (pageAccount == null) {
            return null;
        }
        Page<AccountResponse> accountResponsePage = pageAccount.map(account -> {
            AccountResponse accountResponse = entityToDTO(account);
            return accountResponse;
        });
        return accountResponsePage;
    }

    @SneakyThrows
    private boolean isValidPhoneNumber(String phonenumber) {
        String phonenumberFormat = DataFormatUtils.formatString(phonenumber);
        if (userAccountRepository.existsByPhonenumber(phonenumberFormat, IsDeleted.UNDELETED.getValue()) != 0) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Accounts.PHONENUMBER_EXISTS));
        }
        if (!phonenumber.matches(RegexPattern.REGEX_PHONENUMBER)) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Accounts.PHONENUMBER_INVALID));
        }
        return true;
    }

    @SneakyThrows
    private boolean isValidEmail(String email) {
        String emailFormat = DataFormatUtils.formatString(email);
        if (userAccountRepository.existsByEmail(emailFormat, IsDeleted.UNDELETED.getValue()) != 0) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Accounts.EMAIL_EXISTS));

        }
        if (!email.matches(RegexPattern.REGEX_EMAIL)) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Accounts.EMAIL_INVALID));
        }
        return true;
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

    @SneakyThrows
    private boolean isValidUsername(String username) {
        String usernameFormat = DataFormatUtils.formatString(username);
        if (userAccountRepository.existsByUsername(usernameFormat, IsDeleted.UNDELETED.getValue()) != 0) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Accounts.USERNAME_EXISTS));
        }
        return true;
    }
}
