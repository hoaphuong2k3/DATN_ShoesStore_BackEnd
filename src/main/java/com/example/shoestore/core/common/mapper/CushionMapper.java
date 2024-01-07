package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.product.admin.dto.CushionDTO;
import com.example.shoestore.core.product.admin.dto.request.CushionCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.CushionUpdateRequest;
import com.example.shoestore.entity.Brand;
import com.example.shoestore.entity.Cushion;

import java.util.List;

public interface CushionMapper extends BaseMapper<Cushion, CushionDTO> {

    List<CushionDTO> entityListToDTOList(List<Cushion> entityList);

    Cushion createToEntity(CushionCreateRequest createRequest);

    void updateToEntity(CushionUpdateRequest updateRequest, Cushion entity);

}
