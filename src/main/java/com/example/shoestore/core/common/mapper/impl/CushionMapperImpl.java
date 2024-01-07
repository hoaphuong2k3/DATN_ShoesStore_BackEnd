package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.CushionMapper;
import com.example.shoestore.core.product.admin.dto.CushionDTO;
import com.example.shoestore.core.product.admin.dto.request.CushionCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.CushionUpdateRequest;
import com.example.shoestore.entity.Brand;
import com.example.shoestore.entity.Cushion;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CushionMapperImpl implements CushionMapper {
    @Override
    public Cushion DTOToEntity(CushionDTO dto) {
        return null;
    }

    @Override
    public CushionDTO entityToDTO(Cushion entity) {
        if (entity == null) {
            return null;
        }

        CushionDTO dto = new CushionDTO();

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
    public List<CushionDTO> entityListToDTOList(List<Cushion> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        List<CushionDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity->{
            CushionDTO dto = entityToDTO(entity);
            if (dto != null) {
                dtoList.add(dto);
            }
        });

        return dtoList;
    }

    @Override
    public Cushion createToEntity(CushionCreateRequest createRequest) {
        Cushion.CushionBuilder entityBuilder = Cushion.builder();

        entityBuilder.code(GenerateCode.code("CU"));
        entityBuilder.name(createRequest.getName());
        entityBuilder.isDeleted(Boolean.FALSE);

        return entityBuilder.build();
    }

    @Override
    public void updateToEntity(CushionUpdateRequest updateRequest, Cushion entity) {
        entity.setName(updateRequest.getName());
    }
}
