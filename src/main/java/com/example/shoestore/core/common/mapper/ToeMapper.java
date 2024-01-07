package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.product.admin.dto.ToeDTO;
import com.example.shoestore.core.product.admin.dto.request.ToeCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.ToeUpdateRequest;
import com.example.shoestore.entity.Toe;

import java.util.List;

public interface ToeMapper extends BaseMapper<Toe, ToeDTO> {

    List<ToeDTO> entityListToDTOList(List<Toe> entityList);

    Toe createToEntity(ToeCreateRequest createRequest);

    void updateToEntity(ToeUpdateRequest updateRequest, Toe entity);
}
