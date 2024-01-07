package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.product.admin.dto.SkinTypeDTO;
import com.example.shoestore.core.product.admin.dto.request.SkinTypeCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.SkinTypeUpdateRequest;
import com.example.shoestore.entity.SkinType;

import java.util.List;

public interface SkinTypeMapper extends BaseMapper<SkinType, SkinTypeDTO> {

    List<SkinTypeDTO> entityListToDTOList(List<SkinType> entityList);

    SkinType createToEntity(SkinTypeCreateRequest createRequest);

    void updateToEntity(SkinTypeUpdateRequest updateRequest, SkinType entity);
}
