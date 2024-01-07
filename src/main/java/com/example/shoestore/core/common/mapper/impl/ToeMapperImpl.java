package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.ToeMapper;
import com.example.shoestore.core.product.admin.dto.SkinTypeDTO;
import com.example.shoestore.core.product.admin.dto.ToeDTO;
import com.example.shoestore.core.product.admin.dto.request.ToeCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.ToeUpdateRequest;
import com.example.shoestore.entity.Brand;
import com.example.shoestore.entity.Toe;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ToeMapperImpl implements ToeMapper {
    @Override
    public Toe DTOToEntity(ToeDTO dto) {
        return null;
    }

    @Override
    public ToeDTO entityToDTO(Toe entity) {
        if (entity == null) {
            return null;
        }

        ToeDTO dto = new ToeDTO();

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
    public List<ToeDTO> entityListToDTOList(List<Toe> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        List<ToeDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity->{
            ToeDTO dto = entityToDTO(entity);
            if (dto != null) {
                dtoList.add(dto);
            }
        });

        return dtoList;      }

    @Override
    public Toe createToEntity(ToeCreateRequest createRequest) {
        Toe.ToeBuilder entityBuilder = Toe.builder();

        entityBuilder.code(GenerateCode.code("TO"));
        entityBuilder.name(createRequest.getName());
        entityBuilder.isDeleted(Boolean.FALSE);

        return entityBuilder.build();
    }

    @Override
    public void updateToEntity(ToeUpdateRequest updateRequest, Toe entity) {
        entity.setName(updateRequest.getName());
    }
}
