package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.ImageMapper;
import com.example.shoestore.core.common.mapper.ShoesDetailMapper;
import com.example.shoestore.core.product.admin.dto.ImageDTO;
import com.example.shoestore.core.product.admin.dto.ShoesDetailDTO;
import com.example.shoestore.core.product.admin.dto.ShoesDetailImageDTO;
import com.example.shoestore.core.product.admin.dto.request.ShoesDetailCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.ShoesDetailImportRequest;
import com.example.shoestore.core.product.admin.dto.response.ShoesDetailImportResponse;
import com.example.shoestore.core.product.admin.dto.request.ShoesDetailUpdateRequest;
import com.example.shoestore.entity.Image;
import com.example.shoestore.entity.ShoesDetail;
import com.example.shoestore.infrastructure.utils.DataFormatUtils;
import com.example.shoestore.infrastructure.utils.DataUtils;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import com.example.shoestore.infrastructure.utils.QRCodeUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoesDetailMapperImpl implements ShoesDetailMapper {

    private final ImageMapper imageMapper;

    private final QRCodeUtils utils;

    @Override
    public ShoesDetail DTOToEntity(ShoesDetailDTO dto) {
        return null;
    }

    @Override
    public ShoesDetailDTO entityToDTO(ShoesDetail entity) {

        if (DataUtils.isNull(entity)) {
            return null;
        }

        ShoesDetailDTO dto = new ShoesDetailDTO();

        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setShoesId(entity.getShoesId());
        dto.setColorId(entity.getColorId());
        dto.setSizeId(entity.getSizeId());
        dto.setPrice(entity.getPrice());
        dto.setDiscountPrice(entity.getDiscountPrice());
        dto.setQuantity(entity.getQuantity());
        dto.setStatus(entity.getStatus());
        dto.setQRCodeURI(entity.getQRCodeURI());

        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setCreatedTime(entity.getCreatedTime());
        dto.setUpdatedTime(entity.getUpdatedTime());
        dto.setIsDeleted(entity.getIsDeleted());

        return dto;
    }

    @Override
    public List<ShoesDetailDTO> entitiesToDTOs(List<ShoesDetail> entities) {
        return entities.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShoesDetail> createsToEntities(Long shoesId, List<ShoesDetailCreateRequest> createRequests) {
        return createRequests.stream()
                .map(createRequest -> createToEntity(shoesId, createRequest))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public void updateToEntity(ShoesDetailUpdateRequest updateRequest, ShoesDetail entity) {

        DataFormatUtils.trimStringFields(updateRequest);

        entity.setColorId(updateRequest.getColorId());
        entity.setSizeId(updateRequest.getSizeId());
        entity.setPrice(updateRequest.getPrice());
        entity.setQuantity(updateRequest.getQuantity());
    }

    @SneakyThrows
    @Override
    public ShoesDetail createToEntity(Long shoesId, ShoesDetailCreateRequest createRequest) {

        DataFormatUtils.trimStringFields(createRequest);

        ShoesDetail.ShoesDetailBuilder entityBuild = ShoesDetail.builder();

        entityBuild.code(GenerateCode.code("SHDT_"));
        entityBuild.shoesId(shoesId);
        entityBuild.colorId(createRequest.getColorId());
        entityBuild.sizeId(createRequest.getSizeId());
        entityBuild.price(createRequest.getPrice());
        entityBuild.discountPrice(createRequest.getPrice());
        entityBuild.quantity(createRequest.getQuantity());
        entityBuild.status(createRequest.getStatus());
        entityBuild.QRCodeURI(utils.uploadQRCodeToS3(UUID.randomUUID().toString()));
        entityBuild.isDeleted(Boolean.FALSE);

        return entityBuild.build();
    }

    @SneakyThrows
    @Override
    public ShoesDetail importExcelToEntity(ShoesDetailImportRequest importExcelRequest) {

        DataFormatUtils.trimStringFields(importExcelRequest);

        ShoesDetail.ShoesDetailBuilder entityBuild = ShoesDetail.builder();

        entityBuild.code(GenerateCode.code("SHDT_"));
        entityBuild.shoesId(importExcelRequest.getShoesId());
        entityBuild.colorId(importExcelRequest.getColorId());
        entityBuild.sizeId(importExcelRequest.getSizeId());
        entityBuild.price(importExcelRequest.getPrice());
        entityBuild.discountPrice(importExcelRequest.getPrice());
        entityBuild.quantity(importExcelRequest.getQuantity());
        entityBuild.status(importExcelRequest.getStatus());
        entityBuild.isDeleted(Boolean.FALSE);

        return entityBuild.build();

    }

    @Override
    public List<ShoesDetail> importExcelsToEntities(List<ShoesDetailImportRequest> importExcelRequests) {
        return importExcelRequests.stream()
                .map(importExcelRequest -> importExcelToEntity(importExcelRequest))
                .collect(Collectors.toList());    }

    @Override
    public ShoesDetailImageDTO entitiesToDTOs(List<ShoesDetail> shoesDetails, List<Image> images) {

        List<ShoesDetailDTO> shoesDetailDTOS = this.entitiesToDTOs(shoesDetails);

        List<ImageDTO> imageDTOS = imageMapper.entityListToDTOList(images);

        ShoesDetailImageDTO shoesDetailImageDTO = new ShoesDetailImageDTO();
        shoesDetailImageDTO.setShoesDetailDTOS(shoesDetailDTOS);
        shoesDetailImageDTO.setImageDTOS(imageDTOS);

        return shoesDetailImageDTO;
    }

    @Override
    public ShoesDetailImportResponse toShoesDetailImportExcel(ShoesDetailImportRequest importRequest, String error) {
        if (DataUtils.isNull(importRequest)) {
            return null;
        }

        ShoesDetailImportResponse response = new ShoesDetailImportResponse();

        response.setShoesId(importRequest.getShoesId());
        response.setColorId(importRequest.getColorId());
        response.setSizeId(importRequest.getSizeId());
        response.setPrice(importRequest.getPrice());
        response.setQuantity(importRequest.getQuantity());
        response.setStatus(importRequest.getStatus());
        response.setError(error);

        return response;
    }
}
