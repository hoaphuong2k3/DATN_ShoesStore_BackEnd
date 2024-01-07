package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.account.client.model.request.CreateClientDTO;
import com.example.shoestore.core.account.client.model.request.RegisterClientDTO;
import com.example.shoestore.core.account.client.model.request.UpdateClientDTO;
import com.example.shoestore.core.account.client.model.response.ClientResponse;
import com.example.shoestore.core.account.client.repository.UserClientRepository;
import com.example.shoestore.core.common.mapper.ClientMapper;
import com.example.shoestore.entity.Client;
import com.example.shoestore.infrastructure.constants.*;
import com.example.shoestore.infrastructure.exception.ResourceNotFoundException;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.DataFormatUtils;
import com.example.shoestore.infrastructure.utils.DataUtils;
import com.example.shoestore.infrastructure.utils.DateUtils;
import com.example.shoestore.infrastructure.utils.MessageUtils;
import com.example.shoestore.repository.AddressRepository;
import com.example.shoestore.sercurity.SercurityConfig;
import com.example.shoestore.sercurity.provider.oath2.UserGoogleResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ClientMapperImpl implements ClientMapper {
    @Autowired
    private UserClientRepository userClientRepository;

    @Autowired
    private AddressRepository addressRepository;

    @SneakyThrows
    @Override
    public Client registerDTOToEntity(RegisterClientDTO clientDTO) {
        Client client = new Client();
        if (clientDTO.getPassword().equals(clientDTO.getConfirmPassword())) {
            client.setFullname(DataFormatUtils.formatString(clientDTO.getFullname()));
            if (isValidUsername(clientDTO.getUsername())) {
                client.setUsername(DataFormatUtils.formatString(clientDTO.getUsername()));
            }
            if (isValidPassword(clientDTO.getPassword())) {
                client.setPassword(SercurityConfig.encoder().encode(DataFormatUtils.formatString(clientDTO.getPassword())));
            }
            if (isValidEmail(clientDTO.getEmail())) {
                client.setEmail(DataFormatUtils.formatString(clientDTO.getEmail()));
            }
            if (isValidPhoneNumber(clientDTO.getPhoneNumber())) {
                client.setPhoneNumber(DataFormatUtils.formatString(clientDTO.getPhoneNumber()));
            }
            return client;
        }
        throw new ValidateException(MessageUtils.getMessage(MessageCode.Message.MESSAGE_PASSWORD_MISMATCH));

    }

    @SneakyThrows
    @Override
    public Client updateDTOToEntity(UpdateClientDTO clientDTO) {
        if (clientDTO == null || clientDTO.getId() == null) {
            return null;
        }
        Optional<Client> optionalAccount = userClientRepository.findById(clientDTO.getId());

        if (optionalAccount.isPresent()) {
            Client client = optionalAccount.get();
            client.setId(clientDTO.getId());
            client.setFullname(DataFormatUtils.formatString(clientDTO.getFullname()));
            client.setGender(clientDTO.getGender());
            client.setDateOfBirth(DateUtils.strToLocalDate(clientDTO.getDateOfBirth()));
            if (clientDTO.getEmail().equals(client.getEmail())) {
                client.setEmail(DataFormatUtils.formatString(clientDTO.getEmail()));
            } else if (isValidEmail(clientDTO.getEmail())) {
                client.setEmail(DataFormatUtils.formatString(clientDTO.getEmail()));
            }

            if (clientDTO.getPhoneNumber().equals(client.getPhoneNumber())) {
                client.setPhoneNumber(DataFormatUtils.formatString(clientDTO.getPhoneNumber()));
            } else if (isValidPhoneNumber(clientDTO.getPhoneNumber())) {
                client.setPhoneNumber(DataFormatUtils.formatString(clientDTO.getPhoneNumber()));
            }
            return client;
        }
        throw new ResourceNotFoundException(MessageUtils.getMessage(MessageCode.Accounts.ACCOUNT_NOT_FOUND));

    }

    @Override
    public Page<ClientResponse> pageEntityToDTO(Page<Client> pageClient) {
        if (pageClient == null) {
            return null;
        }

        Page<ClientResponse> clientResponse = pageClient.map(entity -> {
            ClientResponse response = entityToDTO(entity);
            return response;
        });
        return clientResponse;
    }

    @Override
    public Client adminDTOToDTO(CreateClientDTO clientDTO) {
        if (clientDTO == null) {
            return null;
        }
        Client client = new Client();

        client.setFullname(DataFormatUtils.formatString(clientDTO.getFullname()));
        if (isValidUsername(clientDTO.getUsername())) {
            client.setUsername(DataFormatUtils.formatString(clientDTO.getUsername()));
        }

        if (isValidEmail(clientDTO.getEmail())) {
            client.setEmail(DataFormatUtils.formatString(clientDTO.getEmail()));
        }
        if (isValidPhoneNumber(clientDTO.getPhonenumber())) {
            client.setPhoneNumber(DataFormatUtils.formatString(clientDTO.getPhonenumber()));
        }
        return client;
    }

    @SneakyThrows
    @Override
    public Client userGoogleToEntity(UserGoogleResponse userGoogle) {

        Client.ClientBuilder entityBuilder = Client.builder();

        entityBuilder.email(userGoogle.getEmail());
        entityBuilder.fullname(userGoogle.getFullName());

        URL url = new URL(userGoogle.getAvatar());

        entityBuilder.avatar(url.openStream().readAllBytes());

        String gender = userGoogle.getGender();
        if (DataUtils.isNotNull(gender)) {
            entityBuilder.gender(gender.equals("Male") ? Boolean.TRUE : Boolean.FALSE);
        }

        LocalDate dateOfBirth = userGoogle.getDateOfBirth();
        if (DataUtils.isNotNull(dateOfBirth)) {
            entityBuilder.dateOfBirth(dateOfBirth);
        }

        String phoneNumber = userGoogle.getPhoneNumber();
        if (DataUtils.isNotNull(phoneNumber)) {
            entityBuilder.phoneNumber(phoneNumber);
        }

        entityBuilder.isDeleted(Boolean.FALSE);
        entityBuilder.status(Status.ACTIVATE.getValue());
        entityBuilder.idRole(RoleAccount.USER.getValue());

        return entityBuilder.build();
    }

    @Override
    public Client DTOToEntity(ClientResponse dto) {
        return null;
    }

    @Override
    public ClientResponse entityToDTO(Client entity) {
        if (entity == null) {
            return null;
        }


        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setId(entity.getId());
        clientResponse.setFullname(entity.getFullname());
        clientResponse.setGender(entity.getGender());
        clientResponse.setDateOfBirth(entity.getDateOfBirth());
        clientResponse.setEmail(entity.getEmail());
        clientResponse.setAvatar(entity.getAvatar());
        clientResponse.setPhoneNumber(entity.getPhoneNumber());
        clientResponse.setStatus(entity.getStatus());
        clientResponse.setIsDeleted(entity.getIsDeleted());
        clientResponse.setTotalPoints(entity.getTotalPoints());
//        if (entity.getIdAddress() != null) {
//            Address address = addressRepository.findById(entity.getIdAddress()).orElse(null);
//            clientResponse.setDistrictCode(address.getDistrictCode());
//            clientResponse.setProviceCode(address.getProviceCode());
//            clientResponse.setAddressDetail(address.getAddressDetail());
//            clientResponse.setCommuneCode(address.getCommuneCode());
//        }
        return clientResponse;
    }


    @SneakyThrows
    private boolean isValidPhoneNumber(String phonenumber) {
        String phonenumberFormat = DataFormatUtils.formatString(phonenumber);
        if (userClientRepository.existsByPhonenumber(phonenumberFormat, IsDeleted.UNDELETED.getValue()) != 0) {
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
        if (userClientRepository.existsByEmail(emailFormat, IsDeleted.UNDELETED.getValue()) != 0) {
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
        if (userClientRepository.existsByUsername(usernameFormat, IsDeleted.UNDELETED.getValue()) != 0) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Accounts.USERNAME_EXISTS));
        }
        return true;
    }
}
