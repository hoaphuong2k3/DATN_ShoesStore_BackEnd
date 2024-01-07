package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.product.admin.dto.LiningDTO;
import com.example.shoestore.core.product.admin.dto.request.LiningCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.LiningUpdateRequest;
import com.example.shoestore.entity.Lining;

import java.util.List;

public interface LiningMapper extends BaseMapper<Lining, LiningDTO> {

    List<LiningDTO> entityListToDTOList(List<Lining> entityList);

    Lining createToEntity(LiningCreateRequest createRequest);

    void updateToEntity(LiningUpdateRequest updateRequest, Lining entity);

}
