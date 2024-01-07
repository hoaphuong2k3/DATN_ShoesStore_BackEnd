package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.OriginMapper;
import com.example.shoestore.core.product.admin.dto.OriginDTO;
import com.example.shoestore.core.product.admin.dto.request.OriginCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.OriginUpdateRequest;
import com.example.shoestore.entity.Brand;
import com.example.shoestore.entity.Origin;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OriginMapperImpl implements OriginMapper {
    @Override
    public Origin DTOToEntity(OriginDTO dto) {
        return null;
    }

    @Override
    public OriginDTO entityToDTO(Origin entity) {
        if (entity == null) {
            return null;
        }

        OriginDTO dto = new OriginDTO();

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
    public List<OriginDTO> entityListToDTOList(List<Origin> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        List<OriginDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity->{
            OriginDTO dto = entityToDTO(entity);
            if (dto != null) {
                dtoList.add(dto);
            }
        });

        return dtoList;    }

    @Override
    public Origin createToEntity(OriginCreateRequest createRequest) {
        Origin.OriginBuilder entityBuilder = Origin.builder();

        entityBuilder.code(GenerateCode.code("OR"));
        entityBuilder.name(createRequest.getName());
        entityBuilder.isDeleted(Boolean.FALSE);

        return entityBuilder.build();
    }

    @Override
    public void updateToEntity(OriginUpdateRequest updateRequest, Origin entity) {
        entity.setName(updateRequest.getName());
    }
}
