package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.account.client.model.request.CreateClientDTO;
import com.example.shoestore.core.account.client.model.request.RegisterClientDTO;
import com.example.shoestore.core.account.client.model.request.UpdateClientDTO;
import com.example.shoestore.core.account.client.model.response.ClientResponse;
import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.entity.Client;
import com.example.shoestore.sercurity.provider.oath2.UserGoogleResponse;
import org.springframework.data.domain.Page;

public interface ClientMapper extends BaseMapper<Client , ClientResponse> {

    Client registerDTOToEntity(RegisterClientDTO clientDTO);

    Client updateDTOToEntity(UpdateClientDTO clientDTO);

    Page<ClientResponse> pageEntityToDTO(Page<Client> pageClient);
    Client adminDTOToDTO(CreateClientDTO clientDTO);

    Client userGoogleToEntity(UserGoogleResponse userGoogle);

}
