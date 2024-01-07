package com.example.shoestore.core.account.client.service;

import com.example.shoestore.core.account.client.model.request.CreateAddressDTO;
import com.example.shoestore.core.account.client.model.request.UpdateAddressDTO;
import com.example.shoestore.core.account.client.model.response.AddressClientResponse;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import org.springframework.data.domain.Page;

public interface AddressService {

    Page<AddressClientResponse> pageAddress(int page, int size, Long idClient);

    ResponseDTO create(CreateAddressDTO createAddressDTO);

    ResponseDTO update(UpdateAddressDTO updateAddressDTO);

    ResponseDTO delete(Long id);

    ResponseDTO detail(Long id);
}
