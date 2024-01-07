package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.SizeMapper;
import com.example.shoestore.core.product.admin.dto.SizeDTO;
import com.example.shoestore.core.product.admin.dto.request.SizeCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.SizeUpdateRequest;
import com.example.shoestore.entity.Color;
import com.example.shoestore.entity.Size;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SizeMapperImpl implements SizeMapper {
    @Override
    public Size DTOToEntity(SizeDTO dto) {
        return null;
    }

    @Override
    public SizeDTO entityToDTO(Size entity) {
        if (entity == null) {
            return null;
        }

        SizeDTO dto = new SizeDTO();

        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedTime(entity.getCreatedTime());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setUpdatedTime(entity.getUpdatedTime());
        dto.setIsDeleted(entity.getIsDeleted());

        return dto;     }

    @Override
    public List<SizeDTO> entityListToDTOList(List<Size> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        List<SizeDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity->{
            SizeDTO dto = entityToDTO(entity);
            if (dto != null) {
                dtoList.add(dto);
            }
        });

        return dtoList;     }

    @Override
    public Size createToEntity(SizeCreateRequest createRequest) {
        Size.SizeBuilder entityBuilder = Size.builder();

        entityBuilder.code(GenerateCode.code("SZ"));
        entityBuilder.name(createRequest.getName());
        entityBuilder.isDeleted(Boolean.FALSE);

        return entityBuilder.build();
    }

    @Override
    public void updateToEntity(SizeUpdateRequest updateRequest, Size entity) {
        entity.setName(updateRequest.getName());
    }
}
