package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.ColorMapper;
import com.example.shoestore.core.product.admin.dto.ColorDTO;
import com.example.shoestore.core.product.admin.dto.request.ColorCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.ColorUpdateRequest;
import com.example.shoestore.entity.Color;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ColorMapperImpl implements ColorMapper {
    @Override
    public Color DTOToEntity(ColorDTO dto) {
        return null;
    }

    @Override
    public ColorDTO entityToDTO(Color entity) {
        if (entity == null) {
            return null;
        }

        ColorDTO dto = new ColorDTO();

        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedTime(entity.getCreatedTime());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setUpdatedTime(entity.getUpdatedTime());
        dto.setIsDeleted(entity.getIsDeleted());

        return dto;
    }

    @Override
    public List<ColorDTO> entityListToDTOList(List<Color> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        List<ColorDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity->{
            ColorDTO dto = entityToDTO(entity);
            if (dto != null) {
                dtoList.add(dto);
            }
        });

        return dtoList;
    }

    @Override
    public Color createToEntity(ColorCreateRequest createRequest) {

        Color.ColorBuilder entityBuilder = Color.builder();

        entityBuilder.code(GenerateCode.code("CL"));
        entityBuilder.name(createRequest.getName());
        entityBuilder.isDeleted(Boolean.FALSE);

        return entityBuilder.build();
    }

    @Override
    public void updateToEntity(ColorUpdateRequest updateRequest, Color entity) {
        entity.setName(updateRequest.getName());
    }
}
