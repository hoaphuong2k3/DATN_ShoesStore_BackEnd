package com.example.shoestore.core.account.staff.service.impl;

import com.example.shoestore.core.account.staff.model.request.CreateAddressDTO;
import com.example.shoestore.entity.Address;
import com.example.shoestore.infrastructure.constants.IsDeleted;
import com.example.shoestore.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl {

    @Autowired
    private AddressRepository addressRepository;

    public Long getIdAddress(CreateAddressDTO addressDTO) {
        Address address = new Address();
        address.setProviceCode(addressDTO.getProviceCode());
        address.setDistrictCode(addressDTO.getDistrictCode());
        address.setCommuneCode(addressDTO.getCommuneCode());
        address.setAddressDetail(addressDTO.getAddressDetail());
        address.setIsDeleted(IsDeleted.UNDELETED.getValue());
        return addressRepository.save(address).getId();
    }
}
