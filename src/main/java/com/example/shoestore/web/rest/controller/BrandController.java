package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.product.admin.dto.request.BrandCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.BrandSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.BrandUpdateRequest;
import com.example.shoestore.core.product.admin.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAll() {

        ResponseDTO responseDTO = brandService.getALl();

        return ResponseFactory.data(responseDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> create(@RequestBody BrandCreateRequest createRequest){

        ResponseDTO responseDTO = brandService.create(createRequest);

        return ResponseFactory.data(responseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody BrandUpdateRequest updateRequest){

        ResponseDTO responseDTO = brandService.update(id,updateRequest);

        return ResponseFactory.data(responseDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){

        ResponseDTO responseDTO = brandService.findOne(id);

        return ResponseFactory.data(responseDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deletes(@RequestBody List<Long> listId) {
        ResponseDTO responseDTO = brandService.deleteMultipart(listId);
        return ResponseFactory.data(responseDTO);
    }

    @PostMapping("/search")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> search(@RequestBody BrandSearchRequest searchRequest, @ParameterObject Pageable pageable) {
        ResponseDTO responseDTO = brandService.search(searchRequest, pageable);
        return ResponseFactory.data(responseDTO);
    }

}
