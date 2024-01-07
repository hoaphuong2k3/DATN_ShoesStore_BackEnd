package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.product.admin.dto.request.SkinTypeCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.SkinTypeSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.SkinTypeUpdateRequest;
import com.example.shoestore.core.product.admin.service.SkinTypeService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/skin-type")
@RequiredArgsConstructor

public class SkinTypeController {

    private final SkinTypeService skinTypeService;

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAll() {

        ResponseDTO responseDTO = skinTypeService.getALl();

        return ResponseFactory.data(responseDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> create(@RequestBody SkinTypeCreateRequest createRequest){

        ResponseDTO responseDTO = skinTypeService.create(createRequest);

        return ResponseFactory.data(responseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody SkinTypeUpdateRequest updateRequest){

        ResponseDTO responseDTO = skinTypeService.update(id,updateRequest);

        return ResponseFactory.data(responseDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){

        ResponseDTO responseDTO = skinTypeService.findOne(id);

        return ResponseFactory.data(responseDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deletes(@RequestBody List<Long> listId) {
        ResponseDTO responseDTO = skinTypeService.deleteMultipart(listId);
        return ResponseFactory.data(responseDTO);
    }

    @PostMapping("/search")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> search(@RequestBody SkinTypeSearchRequest searchRequest, @ParameterObject Pageable pageable) {
        ResponseDTO responseDTO = skinTypeService.search(searchRequest, pageable);
        return ResponseFactory.data(responseDTO);
    }
}
