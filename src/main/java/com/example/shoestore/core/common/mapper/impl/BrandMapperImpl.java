package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.BrandMapper;
import com.example.shoestore.core.product.admin.dto.BrandDTO;
import com.example.shoestore.core.product.admin.dto.request.BrandCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.BrandUpdateRequest;
import com.example.shoestore.entity.Brand;
import com.example.shoestore.entity.Color;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandMapperImpl implements BrandMapper {
    @Override
    public Brand DTOToEntity(BrandDTO dto) {
        return null;
    }

    @Override
    public BrandDTO entityToDTO(Brand entity) {
        if (entity == null) {
            return null;
        }

        BrandDTO dto = new BrandDTO();

        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedTime(entity.getCreatedTime());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setIsDeleted(entity.getIsDeleted());

        return dto;
    }

    @Override
    public List<BrandDTO> entityListToDTOList(List<Brand> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        List<BrandDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity -> {
            BrandDTO dto = entityToDTO(entity);
            if (dto != null) {
                dtoList.add(dto);
            }
        });

        return dtoList;
    }

    @Override
    public Brand createToEntity(BrandCreateRequest createRequest) {
        Brand.BrandBuilder entityBuilder = Brand.builder();

        entityBuilder.code(GenerateCode.code("BR"));
        entityBuilder.name(createRequest.getName());
        entityBuilder.isDeleted(Boolean.FALSE);

        return entityBuilder.build();

    }

    @Override
    public void updateToEntity(BrandUpdateRequest updateRequest, Brand entity) {
        entity.setName(updateRequest.getName());
    }
}
