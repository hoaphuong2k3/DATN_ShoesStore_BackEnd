package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.DesignStyleMapper;
import com.example.shoestore.core.product.admin.dto.DesignStyleDTO;
import com.example.shoestore.core.product.admin.dto.request.DesignStyleCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.DesignStyleUpdateRequest;
import com.example.shoestore.entity.Brand;
import com.example.shoestore.entity.DesignStyle;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DesignStyleMapperImpl implements DesignStyleMapper {
    @Override
    public DesignStyle DTOToEntity(DesignStyleDTO dto) {
        return null;
    }

    @Override
    public DesignStyleDTO entityToDTO(DesignStyle entity) {
        if (entity == null) {
            return null;
        }

        DesignStyleDTO dto = new DesignStyleDTO();

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
    public List<DesignStyleDTO> entityListToDTOList(List<DesignStyle> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        List<DesignStyleDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity->{
            DesignStyleDTO dto = entityToDTO(entity);
            if (dto != null) {
                dtoList.add(dto);
            }
        });

        return dtoList;    }

    @Override
    public DesignStyle createToEntity(DesignStyleCreateRequest createRequest) {
        DesignStyle.DesignStyleBuilder entityBuilder = DesignStyle.builder();

        entityBuilder.code(GenerateCode.code("DS"));
        entityBuilder.name(createRequest.getName());
        entityBuilder.isDeleted(Boolean.FALSE);

        return entityBuilder.build();
    }

    @Override
    public void updateToEntity(DesignStyleUpdateRequest updateRequest, DesignStyle entity) {
        entity.setName(updateRequest.getName());
    }
}
