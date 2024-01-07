package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.LiningMapper;
import com.example.shoestore.core.product.admin.dto.LiningDTO;
import com.example.shoestore.core.product.admin.dto.request.LiningCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.LiningUpdateRequest;
import com.example.shoestore.entity.Lining;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LiningMapperImpl implements LiningMapper {
    @Override
    public Lining DTOToEntity(LiningDTO dto) {
        return null;
    }

    @Override
    public LiningDTO entityToDTO(Lining entity) {
        if (entity == null) {
            return null;
        }

        LiningDTO dto = new LiningDTO();

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
    public List<LiningDTO> entityListToDTOList(List<Lining> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        List<LiningDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity->{
            LiningDTO dto = entityToDTO(entity);
            if (dto != null) {
                dtoList.add(dto);
            }
        });

        return dtoList;    }

    @Override
    public Lining createToEntity(LiningCreateRequest createRequest) {
        Lining.LiningBuilder entityBuilder = Lining.builder();

        entityBuilder.code(GenerateCode.code("LN"));
        entityBuilder.name(createRequest.getName());
        entityBuilder.isDeleted(Boolean.FALSE);

        return entityBuilder.build();
    }

    @Override
    public void updateToEntity(LiningUpdateRequest updateRequest, Lining entity) {
        entity.setName(updateRequest.getName());
    }
}
