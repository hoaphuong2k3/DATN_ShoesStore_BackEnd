package com.example.shoestore.core.account.client.service.Impl;

import com.example.shoestore.core.account.client.model.request.CreateClientDTO;
import com.example.shoestore.core.account.client.model.request.DeleteClienDTO;
import com.example.shoestore.core.account.client.model.request.UpdateClientDTO;
import com.example.shoestore.core.account.client.model.response.ClientResponse;
import com.example.shoestore.core.account.client.repository.AdminClientRepository;
import com.example.shoestore.core.account.client.service.AdminClientService;
import com.example.shoestore.core.account.staff.model.response.SendEmail;
import com.example.shoestore.core.common.BaseService;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.AccountMapper;
import com.example.shoestore.core.common.mapper.ClientMapper;
import com.example.shoestore.entity.Client;
import com.example.shoestore.infrastructure.constants.*;
import com.example.shoestore.infrastructure.utils.DateUtils;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import com.example.shoestore.sercurity.SercurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AdminClientServiceImpl implements AdminClientService {

//    @Autowired
//    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    private AdminClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;
    private static Client client = new Client();

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AccountMapper mapper;

    @Override
    public Page<ClientResponse> pageClient(String input, String createdTime, Boolean gender, String dateOfBirth, Integer status, int page, int size, String sortField, String sortOrder) {
        PageRequest pageRequest = BaseService.createPageRequest(page, size, sortField, sortOrder);

        Page<Client> pageClient = clientRepository.pageClient(input, DateUtils.strToLocalDate(createdTime),DateUtils.strToLocalDate(dateOfBirth) , status, gender, pageRequest);
        return clientMapper.pageEntityToDTO(pageClient);

    }

    @Override
    public ResponseDTO create(CreateClientDTO clientDTO) {
        String password = GenerateCode.RandomStringGenerator();
        client = clientMapper.adminDTOToDTO(clientDTO);
        client.setPassword(SercurityConfig.encoder().encode(password));
        client.setGender(clientDTO.getGender());
        client.setDateOfBirth(DateUtils.strToLocalDate(clientDTO.getDateOfBirth()));
        client.setIsDeleted(IsDeleted.UNDELETED.getValue());
        client.setIdRole(RoleAccount.USER.getValue());
        client.setStatus(Status.ACTIVATE.getValue());
        client.setCreatedTime(LocalDateTime.now());
        Client saveClient = clientRepository.save(client);
        SendEmail sendEmail = mapper.sendEmailClientEntityToDTO(saveClient, password);
        this.sendToEmail(sendEmail);
        return ResponseDTO.success(clientMapper.entityToDTO(clientRepository.save(client)));
    }

    @Override
    public ResponseDTO update(UpdateClientDTO clientDTO) {

        if (clientDTO == null) {
            return null;
        }
        clientDTO.setUpdateTime(LocalDateTime.now());
        clientRepository.updateAccount(clientDTO);
        return ResponseDTO.success();
    }

    @Override
    public ResponseDTO delete(DeleteClienDTO deleteClienDTO) {
        if (deleteClienDTO.getId().isEmpty()) {
            return null;
        }
        clientRepository.deleteClient(IsDeleted.DELETED.getValue(), deleteClienDTO.getId());
        return ResponseDTO.success(ResponseMess.SUCCESS);
    }

    @Override
    public ResponseDTO detail(Long id) {
        if (id == null) {
            return null;
        }
        return ResponseDTO.success(clientRepository.findAccount(id));
    }

    @Override
    public ResponseDTO updateStatus(Integer status, Long id) {
        clientRepository.updateStatus(status, id);
        return ResponseDTO.success();


    }

//    @RabbitListener(queues = {RabbitMQConfig.TOPIC_QUEUE_CLIENT})
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

}
