package com.example.shoestore.core.product.admin.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.product.admin.dto.request.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandService {

    ResponseDTO getALl();

    ResponseDTO create(BrandCreateRequest createRequest);

    ResponseDTO update(Long id, BrandUpdateRequest updateRequest);

    ResponseDTO findOne(Long id);

    ResponseDTO deleteMultipart(List<Long> ids);

    ResponseDTO search(BrandSearchRequest searchRequest, Pageable pageable);

}
