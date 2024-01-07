package com.example.shoestore.core.product.admin.service;

import com.example.shoestore.core.common.dto.response.FileResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.product.admin.dto.request.ShoesCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.ShoesExportExcelRequest;
import com.example.shoestore.core.product.admin.dto.request.ShoesSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.ShoesUpdateRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ShoesService {
    ResponseDTO create(ShoesCreateRequest createRequest, MultipartFile file);

    ResponseDTO update(Long id,ShoesUpdateRequest updateRequest, MultipartFile file,Boolean isChange);

    ResponseDTO search(ShoesSearchRequest searchRequest, Pageable pageable);

    ResponseDTO findOne(Long id);

    ResponseDTO deleteMultipart(List<Long> ids);

    FileResponseDTO exportExel(List<ShoesExportExcelRequest> searchResponses);

    FileResponseDTO exportPDF(List<ShoesExportExcelRequest> searchResponses);

    FileResponseDTO exportPattern();

    FileResponseDTO  importExcel(MultipartFile fileExcel);

}
