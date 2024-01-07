package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.product.admin.dto.ShoesDetailDTO;
import com.example.shoestore.core.product.admin.dto.ShoesDetailImageDTO;
import com.example.shoestore.core.product.admin.dto.request.ShoesDetailCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.ShoesDetailImportRequest;
import com.example.shoestore.core.product.admin.dto.response.ShoesDetailImportResponse;
import com.example.shoestore.core.product.admin.dto.request.ShoesDetailUpdateRequest;
import com.example.shoestore.entity.Image;
import com.example.shoestore.entity.ShoesDetail;

import java.util.List;

public interface ShoesDetailMapper extends BaseMapper<ShoesDetail, ShoesDetailDTO> {

    List<ShoesDetailDTO> entitiesToDTOs(List<ShoesDetail> entities);

    ShoesDetail createToEntity(Long shoesId,ShoesDetailCreateRequest createRequest);

    List<ShoesDetail> createsToEntities(Long shoesId,List<ShoesDetailCreateRequest> createRequests);

    void updateToEntity(ShoesDetailUpdateRequest updateRequest,ShoesDetail entity);

    ShoesDetail importExcelToEntity(ShoesDetailImportRequest importExcelRequest);

    List<ShoesDetail> importExcelsToEntities(List<ShoesDetailImportRequest> importExcelRequests);

    ShoesDetailImageDTO entitiesToDTOs(List<ShoesDetail> shoesDetails, List<Image> images);

    ShoesDetailImportResponse toShoesDetailImportExcel(ShoesDetailImportRequest importRequest, String error);
}
