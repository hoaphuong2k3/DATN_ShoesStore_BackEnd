package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.FileResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.product.admin.dto.request.*;
import com.example.shoestore.core.product.admin.service.ShoesService;
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
@RequestMapping("/admin/shoes")
@RequiredArgsConstructor
public class ShoesController {

    private final ShoesService shoesService;
    private final Validator validator;

    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> create(@Valid @ModelAttribute ShoesFileCreateRequest createRequest) {

        ShoesCreateRequest request = createRequest.getData(validator);
        MultipartFile file = createRequest.getFile();
        ResponseDTO responseDTO = shoesService.create(request, file);
        return ResponseFactory.data(responseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @Valid @ModelAttribute ShoesFileUpdateRequest updateRequest) {

        ShoesUpdateRequest request = updateRequest.getData(validator);
        MultipartFile file = updateRequest.getFile();
        Boolean isChange = updateRequest.getIsChange();
        ResponseDTO responseDTO = shoesService.update(id, request, file,isChange);
        return ResponseFactory.data(responseDTO);
    }

    @PostMapping("/search")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> search(@RequestBody ShoesSearchRequest searchRequest, @ParameterObject Pageable pageable) {

        ResponseDTO responseDTO = shoesService.search(searchRequest, pageable);
        return ResponseFactory.data(responseDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id) {

        ResponseDTO responseDTO = shoesService.findOne(id);
        return ResponseFactory.data(responseDTO);
    }
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deletes(@RequestBody List<Long> listId) {
        ResponseDTO responseDTO = shoesService.deleteMultipart(listId);
        return ResponseFactory.data(responseDTO);
    }

    @PostMapping("/export/excel")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ByteArrayResource> exportToExcel(@RequestBody List<ShoesExportExcelRequest> shoesExport) {

        FileResponseDTO fileResponseDTO = shoesService.exportExel(shoesExport);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,  "attachment; filename=" + fileResponseDTO.getFileName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileResponseDTO.getByteArrayResource());
    }

    @GetMapping("/export/pattern")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ByteArrayResource> exportPattern() {

        FileResponseDTO fileResponseDTO = shoesService.exportPattern();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,  "attachment; filename=" + fileResponseDTO.getFileName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileResponseDTO.getByteArrayResource());
    }

    @PostMapping("/import-excel")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ByteArrayResource> importExcel(@RequestParam("file") MultipartFile fileExcel) {

        FileResponseDTO fileResponseDTO = shoesService.importExcel(fileExcel);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,  "attachment; filename=" + fileResponseDTO.getFileName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileResponseDTO.getByteArrayResource());

    }

}
