package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.dto.ImageUploadDTO;
import com.example.shoestore.core.common.mapper.ShoesMapper;
import com.example.shoestore.core.product.admin.dto.ShoesDTO;
import com.example.shoestore.core.product.admin.dto.request.ShoesCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.ShoesImportRequest;
import com.example.shoestore.core.product.admin.dto.request.ShoesUpdateRequest;
import com.example.shoestore.core.product.admin.dto.response.ShoesDetailImportResponse;
import com.example.shoestore.core.product.admin.dto.response.ShoesImportResponse;
import com.example.shoestore.entity.Shoes;
import com.example.shoestore.infrastructure.utils.DataUtils;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import com.example.shoestore.infrastructure.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoesMapperImpl implements ShoesMapper {

    private final ImageUtils imageUtils;

    @Override
    public Shoes DTOToEntity(ShoesDTO dto) {
        return null;
    }

    @Override
    public ShoesDTO entityToDTO(Shoes entity) {

        if (DataUtils.isNull(entity)) {
            return null;
        }

        ShoesDTO dto = new ShoesDTO();

        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setBrandId(entity.getBrandId());
        dto.setOriginId(entity.getOriginId());
        dto.setDesignStyleId(entity.getDesignStyleId());
        dto.setSkinTypeId(entity.getSkinTypeId());
        dto.setSoleId(entity.getSoleId());
        dto.setLiningId(entity.getLiningId());
        dto.setToeId(entity.getToeId());
        dto.setCushionId(entity.getCushionId());
        dto.setImgURI(entity.getImgURI());
        dto.setImgName(entity.getImgName());
        dto.setDescription(entity.getDescription());

        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setCreatedTime(entity.getCreatedTime());
        dto.setUpdatedTime(entity.getUpdatedTime());
        dto.setIsDeleted(entity.getIsDeleted());

        return dto;
    }

    @Override
    public Shoes createToEntity(ShoesCreateRequest createRequest, MultipartFile file) {

        Shoes.ShoesBuilder entityBuild = Shoes.builder();

        entityBuild.code(GenerateCode.code("SH"));
        entityBuild.name(createRequest.getName());

        entityBuild.brandId(createRequest.getBrandId());
        entityBuild.originId(createRequest.getOriginId());
        entityBuild.designStyleId(createRequest.getDesignStyleId());
        entityBuild.skinTypeId(createRequest.getSkinTypeId());
        entityBuild.soleId(createRequest.getSoleId());
        entityBuild.liningId(createRequest.getLiningId());
        entityBuild.toeId(createRequest.getToeId());
        entityBuild.cushionId(createRequest.getCushionId());
        entityBuild.description(createRequest.getDescription());

        ImageUploadDTO uploadDTO = imageUtils.uploadImage(file);
        entityBuild.imgURI(uploadDTO.getImgURI());
        entityBuild.imgName(uploadDTO.getImgName());

        entityBuild.isDeleted(Boolean.FALSE);

        return entityBuild.build();
    }

    @Override
    public void updateToEntity(ShoesUpdateRequest updateRequest, MultipartFile file, Shoes entity, Boolean isChange) {

        entity.setName(updateRequest.getName());
        entity.setBrandId(updateRequest.getBrandId());
        entity.setOriginId(updateRequest.getOriginId());
        entity.setDesignStyleId(updateRequest.getDesignStyleId());
        entity.setSkinTypeId(updateRequest.getSkinTypeId());
        entity.setSoleId(updateRequest.getSoleId());
        entity.setLiningId(updateRequest.getLiningId());
        entity.setToeId(updateRequest.getToeId());
        entity.setCushionId(updateRequest.getCushionId());
        entity.setDescription(updateRequest.getDescription());

        if(DataUtils.isNotNull(isChange)){
            ImageUploadDTO uploadDTO = imageUtils.uploadImage(file);
            entity.setImgURI(uploadDTO.getImgURI());
            entity.setImgName(uploadDTO.getImgName());
        }

    }

    @Override
    public List<ShoesDTO> entityListToDTOList(List<Shoes> entityList) {

        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        List<ShoesDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity -> {
            ShoesDTO dto = entityToDTO(entity);
            if (dto != null) {
                dtoList.add(dto);
            }
        });

        return dtoList;
    }

    @Override
    public Shoes importExcelToEntity(ShoesImportRequest importExcelRequest) {

        Shoes.ShoesBuilder entityBuild = Shoes.builder();

        entityBuild.code(GenerateCode.code("SH"));
        entityBuild.name(importExcelRequest.getName());

        entityBuild.brandId(importExcelRequest.getBrandId());
        entityBuild.originId(importExcelRequest.getOriginId());
        entityBuild.designStyleId(importExcelRequest.getDesignStyleId());
        entityBuild.skinTypeId(importExcelRequest.getSkinTypeId());
        entityBuild.soleId(importExcelRequest.getSoleId());
        entityBuild.liningId(importExcelRequest.getLiningId());
        entityBuild.toeId(importExcelRequest.getToeId());
        entityBuild.cushionId(importExcelRequest.getCushionId());
        entityBuild.description(importExcelRequest.getDescription());

        entityBuild.imgURI(importExcelRequest.getImgURI());
        entityBuild.imgName(importExcelRequest.getImgName());

        entityBuild.isDeleted(Boolean.FALSE);

        return entityBuild.build();
    }

    @Override
    public List<Shoes> importExcelsToEntities(List<ShoesImportRequest> importExcelRequests) {
        return importExcelRequests.stream()
                .map(importExcelRequest -> importExcelToEntity(importExcelRequest))
                .collect(Collectors.toList());
    }

    @Override
    public ShoesImportResponse toShoesDetailImportExcel(ShoesImportRequest importRequest,String error) {

        if (DataUtils.isNull(importRequest)) {
            return null;
        }

        ShoesImportResponse response = new ShoesImportResponse();

        response.setBrandId(importRequest.getBrandId());
        response.setOriginId(importRequest.getOriginId());
        response.setDesignStyleId(importRequest.getDesignStyleId());
        response.setSkinTypeId(importRequest.getSkinTypeId());
        response.setSoleId(importRequest.getSoleId());
        response.setLiningId(importRequest.getLiningId());
        response.setToeId(importRequest.getToeId());
        response.setCushionId(importRequest.getCushionId());
        response.setDescription(importRequest.getDescription());

        response.setImgURI(importRequest.getImgURI());
        response.setImgName(importRequest.getImgName());
        response.setError(error);

        return response;
    }
}

