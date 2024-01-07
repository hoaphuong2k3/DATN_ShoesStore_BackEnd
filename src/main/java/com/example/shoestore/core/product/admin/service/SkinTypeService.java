package com.example.shoestore.core.product.admin.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.product.admin.dto.request.SkinTypeCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.SkinTypeSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.SkinTypeUpdateRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SkinTypeService {

    ResponseDTO getALl();

    ResponseDTO create(SkinTypeCreateRequest createRequest);

    ResponseDTO update(Long id, SkinTypeUpdateRequest updateRequest);

    ResponseDTO findOne(Long id);

    ResponseDTO deleteMultipart(List<Long> ids);

    ResponseDTO search(SkinTypeSearchRequest searchRequest, Pageable pageable);

}
