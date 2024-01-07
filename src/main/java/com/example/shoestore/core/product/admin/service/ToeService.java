package com.example.shoestore.core.product.admin.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.product.admin.dto.request.ToeCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.ToeSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.ToeUpdateRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ToeService {
    ResponseDTO getALl();

    ResponseDTO create(ToeCreateRequest createRequest);

    ResponseDTO update(Long id, ToeUpdateRequest updateRequest);

    ResponseDTO findOne(Long id);

    ResponseDTO deleteMultipart(List<Long> ids);

    ResponseDTO search(ToeSearchRequest searchRequest, Pageable pageable);

}
