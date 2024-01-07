package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.FileResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.product.admin.dto.request.*;
import com.example.shoestore.core.product.admin.service.ShoesDetailService;
import com.example.shoestore.core.product.admin.dto.request.ShoesDetailReportRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/shoesdetail")
@RequiredArgsConstructor
public class ShoesDetailController {

    private final ShoesDetailService shoesDetailService;

    private final Validator validator;

    @PostMapping("/{shoesId}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> create(@PathVariable("shoesId") Long shoesId,@Valid @ModelAttribute ShoesDetailCreateMultipartRequest createMultipartRequest) {

        List<ShoesDetailCreateRequest> createRequests = createMultipartRequest.getData(validator);

        ResponseDTO responseDTO = shoesDetailService.createMultipart(shoesId, createRequests,createMultipartRequest.getFiles());

        return ResponseFactory.data(responseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> update(@Valid @PathVariable("id") Long id, @RequestBody ShoesDetailUpdateRequest updateRequest) {
        ResponseDTO responseDTO = shoesDetailService.update(id, updateRequest);
        return ResponseFactory.data(responseDTO);
    }

    @PostMapping("/search/{idShoes}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> search(@PathVariable("idShoes") Long idShoes, @RequestBody ShoesDetailSearchRequest searchRequest, @ParameterObject Pageable pageable) {
        ResponseDTO responseDTO = shoesDetailService.search(idShoes, searchRequest, pageable);
        return ResponseFactory.data(responseDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id) {
        ResponseDTO responseDTO = shoesDetailService.findOne(id);
        return ResponseFactory.data(responseDTO);
    }

    @GetMapping("/qr/{qrCode}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> findOneByQRCode(@PathVariable("qrCode") String qrCode) {
        ResponseDTO responseDTO = shoesDetailService.findOneByQRCode(qrCode);
        return ResponseFactory.data(responseDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deletes(@RequestBody List<Long> listId) {
        ResponseDTO responseDTO = shoesDetailService.deleteMultipart(listId);
        return ResponseFactory.data(responseDTO);
    }

    @PutMapping("/on-business/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> onBusiness(@PathVariable("id") Long id) {
        ResponseDTO responseDTO = shoesDetailService.onBusiness(id);
        return ResponseFactory.data(responseDTO);
    }

    @PutMapping("/stop-business/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> stopBusiness(@PathVariable("id") Long id) {
        ResponseDTO responseDTO = shoesDetailService.stopBusiness(id);
        return ResponseFactory.data(responseDTO);
    }

    @PostMapping("/export/excel")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ByteArrayResource> exportToExcel(@RequestBody List<ShoesDetailExportExcelRequest> exportRequests) {

        FileResponseDTO fileResponseDTO = shoesDetailService.exportExel(exportRequests);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileResponseDTO.getFileName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileResponseDTO.getByteArrayResource());
    }

    @PostMapping("/export/pdf/{shoesId}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ByteArrayResource> exportToPDF(@PathVariable("shoesId") Long shoesId,@RequestBody List<ShoesDetailExportPDFRequest> exportRequests) {

        FileResponseDTO fileResponseDTO = shoesDetailService.exportPDF(shoesId,exportRequests);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileResponseDTO.getFileName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileResponseDTO.getByteArrayResource());
    }

    @GetMapping("/export/pattern")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> exportPatternExcel() {

        FileResponseDTO fileResponseDTO = shoesDetailService.exportPattern();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,  "attachment; filename=" + fileResponseDTO.getFileName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileResponseDTO.getByteArrayResource());
    }


    @PostMapping("/import-excel")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ByteArrayResource> importExcelToDB(@RequestParam("file") MultipartFile fileExcel) {

        FileResponseDTO fileResponseDTO = shoesDetailService.importExcel(fileExcel);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,  "attachment; filename=" + fileResponseDTO.getFileName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileResponseDTO.getByteArrayResource());
    }

    @PostMapping("/report/pattern/{shoesId}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> report(@PathVariable("shoesId") Long shoesId, @RequestBody ShoesDetailReportRequest request) {

        ResponseDTO responseDTO =  shoesDetailService.reportByShoesIdToDoc(shoesId,request);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/report")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> report() {

        ResponseDTO responseDTO =  shoesDetailService.reportAllToExcel();

        return ResponseEntity.ok(responseDTO);
    }

}
