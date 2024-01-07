package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.product.admin.dto.SoleDTO;
import com.example.shoestore.core.product.admin.dto.request.SoleCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.SoleUpdateRequest;
import com.example.shoestore.entity.Sole;

import java.util.List;

public interface SoleMapper extends BaseMapper<Sole, SoleDTO> {

    List<SoleDTO> entityListToDTOList(List<Sole> entityList);

    Sole createToEntity(SoleCreateRequest createRequest);

    void updateToEntity(SoleUpdateRequest updateRequest, Sole entity);
}
