package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.product.admin.dto.DesignStyleDTO;
import com.example.shoestore.core.product.admin.dto.request.DesignStyleCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.DesignStyleUpdateRequest;
import com.example.shoestore.entity.DesignStyle;

import java.util.List;

public interface DesignStyleMapper extends BaseMapper<DesignStyle, DesignStyleDTO> {

    List<DesignStyleDTO> entityListToDTOList(List<DesignStyle> entityList);

    DesignStyle createToEntity(DesignStyleCreateRequest createRequest);

    void updateToEntity(DesignStyleUpdateRequest updateRequest, DesignStyle entity);

}
