package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.SoleMapper;
import com.example.shoestore.core.product.admin.dto.SoleDTO;
import com.example.shoestore.core.product.admin.dto.request.SoleCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.SoleUpdateRequest;
import com.example.shoestore.entity.Sole;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SoleMapperImpl implements SoleMapper {
    @Override
    public Sole DTOToEntity(SoleDTO dto) {
        return null;
    }

    @Override
    public SoleDTO entityToDTO(Sole entity) {
        if (entity == null) {
            return null;
        }

        SoleDTO dto = new SoleDTO();

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
    public List<SoleDTO> entityListToDTOList(List<Sole> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        List<SoleDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity->{
            SoleDTO dto = entityToDTO(entity);
            if (dto != null) {
                dtoList.add(dto);
            }
        });

        return dtoList;      }

    @Override
    public Sole createToEntity(SoleCreateRequest createRequest) {
        Sole.SoleBuilder entityBuilder = Sole.builder();

        entityBuilder.code(GenerateCode.code("SL"));
        entityBuilder.name(createRequest.getName());
        entityBuilder.isDeleted(Boolean.FALSE);

        return entityBuilder.build();
    }

    @Override
    public void updateToEntity(SoleUpdateRequest updateRequest, Sole entity) {
        entity.setName(updateRequest.getName());
    }
}
