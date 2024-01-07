package com.example.shoestore.core.account.client.service;

import com.example.shoestore.core.account.client.model.request.CreateClientDTO;
import com.example.shoestore.core.account.client.model.request.DeleteClienDTO;
import com.example.shoestore.core.account.client.model.request.UpdateClientDTO;
import com.example.shoestore.core.account.client.model.response.ClientResponse;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import org.springframework.data.domain.Page;

public interface AdminClientService {

    Page<ClientResponse> pageClient( String input, String createdTime, Boolean gender,String dateOfBirth, Integer status ,int page, int size, String sortField, String sortOrder);

    ResponseDTO create(CreateClientDTO clientDTO);

    ResponseDTO update(UpdateClientDTO clientDTO);

    ResponseDTO delete(DeleteClienDTO deleteClienDTO);

    ResponseDTO detail(Long id);

    ResponseDTO updateStatus(Integer status, Long id);
}
