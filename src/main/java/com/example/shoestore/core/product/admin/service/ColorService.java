package com.example.shoestore.core.product.admin.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.product.admin.dto.request.ColorCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.ColorSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.ColorUpdateRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ColorService {

    ResponseDTO getALl();

    ResponseDTO create(ColorCreateRequest createRequest);

    ResponseDTO update(Long id, ColorUpdateRequest updateRequest);

    ResponseDTO findOne(Long id);

    ResponseDTO deleteMultipart(List<Long> ids);

    ResponseDTO search(ColorSearchRequest searchRequest, Pageable pageable);

}
