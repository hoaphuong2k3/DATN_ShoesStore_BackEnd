package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.product.admin.dto.ColorDTO;
import com.example.shoestore.core.product.admin.dto.request.ColorCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.ColorUpdateRequest;
import com.example.shoestore.entity.Color;

import java.util.List;

public interface ColorMapper extends BaseMapper<Color, ColorDTO> {

    List<ColorDTO> entityListToDTOList(List<Color> entityList);

    Color createToEntity(ColorCreateRequest createRequest);

    void updateToEntity(ColorUpdateRequest updateRequest,Color entity);

}
