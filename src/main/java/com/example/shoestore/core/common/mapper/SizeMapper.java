package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.product.admin.dto.SizeDTO;
import com.example.shoestore.core.product.admin.dto.request.SizeCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.SizeUpdateRequest;
import com.example.shoestore.entity.Size;

import java.util.List;

public interface SizeMapper extends BaseMapper<Size, SizeDTO> {

    List<SizeDTO> entityListToDTOList(List<Size> entityList);

    Size createToEntity(SizeCreateRequest createRequest);

    void updateToEntity(SizeUpdateRequest updateRequest, Size entity);
}
