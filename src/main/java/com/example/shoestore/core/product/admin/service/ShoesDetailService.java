package com.example.shoestore.core.product.admin.service;

import com.example.shoestore.core.common.dto.response.FileResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.product.admin.dto.request.*;
import com.example.shoestore.core.product.admin.dto.request.ShoesDetailReportRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ShoesDetailService {

    ResponseDTO createMultipart(Long shoesId, List<ShoesDetailCreateRequest> createRequest,List<MultipartFile> files);

    ResponseDTO update(Long id, ShoesDetailUpdateRequest updateRequest);

    ResponseDTO search(Long idShoes,ShoesDetailSearchRequest searchRequest, Pageable pageable);

    ResponseDTO findOne(Long id);

    ResponseDTO findOneByQRCode(String qrCode);

    ResponseDTO deleteMultipart(List<Long> ids);

    ResponseDTO onBusiness(Long id);

    ResponseDTO stopBusiness(Long id);

    FileResponseDTO importExcel(MultipartFile fileExcel);

    FileResponseDTO exportExel(List<ShoesDetailExportExcelRequest> searchResponses);

    FileResponseDTO exportPDF(Long shoesId ,List<ShoesDetailExportPDFRequest> exportResponses);

    FileResponseDTO exportPattern();

    ResponseDTO reportByShoesIdToDoc(Long shoesId, ShoesDetailReportRequest request);

    ResponseDTO reportAllToExcel();

}
