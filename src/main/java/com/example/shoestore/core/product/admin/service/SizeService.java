package com.example.shoestore.core.product.admin.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.product.admin.dto.request.SizeCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.SizeSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.SizeUpdateRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SizeService {

    ResponseDTO getALl();

    ResponseDTO create(SizeCreateRequest createRequest);

    ResponseDTO update(Long id, SizeUpdateRequest updateRequest);

    ResponseDTO findOne(Long id);

    ResponseDTO deleteMultipart(List<Long> ids);

    ResponseDTO search(SizeSearchRequest searchRequest, Pageable pageable);

}
