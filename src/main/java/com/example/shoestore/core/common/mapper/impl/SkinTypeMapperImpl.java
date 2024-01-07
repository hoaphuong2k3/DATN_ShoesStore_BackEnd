package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.SkinTypeMapper;
import com.example.shoestore.core.product.admin.dto.SkinTypeDTO;
import com.example.shoestore.core.product.admin.dto.request.SkinTypeCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.SkinTypeUpdateRequest;
import com.example.shoestore.entity.SkinType;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkinTypeMapperImpl implements SkinTypeMapper {
    @Override
    public SkinType DTOToEntity(SkinTypeDTO dto) {
        return null;
    }

    @Override
    public SkinTypeDTO entityToDTO(SkinType entity) {
        if (entity == null) {
            return null;
        }

        SkinTypeDTO dto = new SkinTypeDTO();

        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedTime(entity.getCreatedTime());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setUpdatedTime(entity.getUpdatedTime());
        dto.setIsDeleted(entity.getIsDeleted());

        return dto;      }

    @Override
    public List<SkinTypeDTO> entityListToDTOList(List<SkinType> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        List<SkinTypeDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity->{
            SkinTypeDTO dto = entityToDTO(entity);
            if (dto != null) {
                dtoList.add(dto);
            }
        });

        return dtoList;    }

    @Override
    public SkinType createToEntity(SkinTypeCreateRequest createRequest) {
        SkinType.SkinTypeBuilder entityBuilder = SkinType.builder();

        entityBuilder.code(GenerateCode.code("SKT"));
        entityBuilder.name(createRequest.getName());
        entityBuilder.isDeleted(Boolean.FALSE);

        return entityBuilder.build();
    }

    @Override
    public void updateToEntity(SkinTypeUpdateRequest updateRequest, SkinType entity) {
        entity.setName(updateRequest.getName());
    }
}
