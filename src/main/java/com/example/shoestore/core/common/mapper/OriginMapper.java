package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.product.admin.dto.OriginDTO;
import com.example.shoestore.core.product.admin.dto.request.OriginCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.OriginUpdateRequest;
import com.example.shoestore.entity.Origin;

import java.util.List;

public interface OriginMapper extends BaseMapper<Origin, OriginDTO> {

    List<OriginDTO> entityListToDTOList(List<Origin> entityList);

    Origin createToEntity(OriginCreateRequest createRequest);

    void updateToEntity(OriginUpdateRequest updateRequest, Origin entity);
}
