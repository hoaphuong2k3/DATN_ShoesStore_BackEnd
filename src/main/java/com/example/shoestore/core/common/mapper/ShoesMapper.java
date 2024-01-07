package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.product.admin.dto.ShoesDTO;
import com.example.shoestore.core.product.admin.dto.request.ShoesCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.ShoesImportRequest;
import com.example.shoestore.core.product.admin.dto.request.ShoesUpdateRequest;
import com.example.shoestore.core.product.admin.dto.response.ShoesImportResponse;
import com.example.shoestore.entity.Shoes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ShoesMapper extends BaseMapper<Shoes, ShoesDTO> {

    Shoes createToEntity(ShoesCreateRequest createRequest, MultipartFile file);

    void updateToEntity(ShoesUpdateRequest updateRequest, MultipartFile file, Shoes entity, Boolean isChange);

    List<ShoesDTO> entityListToDTOList(List<Shoes> entityList);

    Shoes importExcelToEntity(ShoesImportRequest importExcelRequest);

    List<Shoes> importExcelsToEntities(List<ShoesImportRequest> importExcelRequests);

    ShoesImportResponse toShoesDetailImportExcel(ShoesImportRequest importRequest,String error);

}
