package com.example.shoestore.core.account.client.service.Impl;

import com.example.shoestore.core.account.client.model.request.CreateAddressDTO;
import com.example.shoestore.core.account.client.model.request.UpdateAddressDTO;
import com.example.shoestore.core.account.client.repository.UserAddressRepository;
import com.example.shoestore.core.account.client.model.response.AddressClientResponse;
import com.example.shoestore.core.account.client.service.AddressService;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.entity.Address;
import com.example.shoestore.infrastructure.constants.IsDeleted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class AddressClientServiceImpl implements AddressService {
    @Autowired
    private UserAddressRepository addressRepository;

    @Override
    public Page<AddressClientResponse> pageAddress(int page, int size, Long idClient) {
        return addressRepository.pageAddress(idClient, PageRequest.of(page, size));
    }

    @Override
    public ResponseDTO create(CreateAddressDTO createAddressDTO) {
        Address address = new Address();
        address.setProviceCode(createAddressDTO.getProviceCode());
        address.setDistrictCode(createAddressDTO.getDistrictCode());
        address.setCommuneCode(createAddressDTO.getCommuneCode());
        address.setAddressDetail(createAddressDTO.getAddressDetail());
        address.setIdClient(createAddressDTO.getIdClient());
        address.setIsDeleted(IsDeleted.UNDELETED.getValue());
        Address saveAddress = addressRepository.save(address);
        return ResponseDTO.success(saveAddress);
    }

    @Override
    public ResponseDTO update(UpdateAddressDTO updateAddressDTO) {
        Address address = new Address();
        address.setId(updateAddressDTO.getId());
        address.setProviceCode(updateAddressDTO.getProviceCode());
        address.setDistrictCode(updateAddressDTO.getDistrictCode());
        address.setCommuneCode(updateAddressDTO.getCommuneCode());
        address.setAddressDetail(updateAddressDTO.getAddressDetail());
        address.setIdClient(updateAddressDTO.getIdClient());
        address.setIsDeleted(IsDeleted.UNDELETED.getValue());
        Address updateAddress = addressRepository.save(address);
        return ResponseDTO.success(updateAddress);
    }

    @Override
    public ResponseDTO delete(Long id) {
        Address address = new Address();
        address.setId(id);
        address.setIsDeleted(IsDeleted.DELETED.getValue());
        addressRepository.save(address);
        return ResponseDTO.success();
    }

    @Override
    public ResponseDTO detail(Long id) {
        return ResponseDTO.success(addressRepository.findById(id).orElse(null));
    }
}
