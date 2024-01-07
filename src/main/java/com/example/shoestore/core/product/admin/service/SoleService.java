package com.example.shoestore.core.product.admin.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.product.admin.dto.request.SoleCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.SoleSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.SoleUpdateRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SoleService {

    ResponseDTO getALl();

    ResponseDTO create(SoleCreateRequest createRequest);

    ResponseDTO update(Long id, SoleUpdateRequest updateRequest);

    ResponseDTO findOne(Long id);

    ResponseDTO deleteMultipart(List<Long> ids);

    ResponseDTO search(SoleSearchRequest searchRequest, Pageable pageable);

}
