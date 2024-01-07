package com.example.shoestore.core.product.admin.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.product.admin.dto.request.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CushionService {

    ResponseDTO getALl();

    ResponseDTO create(CushionCreateRequest createRequest);

    ResponseDTO update(Long id, CushionUpdateRequest updateRequest);

    ResponseDTO findOne(Long id);

    ResponseDTO deleteMultipart(List<Long> ids);

    ResponseDTO search(CushionSearchRequest searchRequest, Pageable pageable);

}
