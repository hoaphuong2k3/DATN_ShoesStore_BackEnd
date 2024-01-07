package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.product.admin.dto.request.*;
import com.example.shoestore.core.product.admin.service.CushionService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/cushion")
@RequiredArgsConstructor

public class CushionController {

    private final CushionService cushionService;

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAll() {

        ResponseDTO responseDTO = cushionService.getALl();

        return ResponseFactory.data(responseDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> create(@RequestBody CushionCreateRequest createRequest){

        ResponseDTO responseDTO = cushionService.create(createRequest);

        return ResponseFactory.data(responseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody CushionUpdateRequest updateRequest){

        ResponseDTO responseDTO = cushionService.update(id,updateRequest);

        return ResponseFactory.data(responseDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){

        ResponseDTO responseDTO = cushionService.findOne(id);

        return ResponseFactory.data(responseDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deletes(@RequestBody List<Long> listId) {
        ResponseDTO responseDTO = cushionService.deleteMultipart(listId);
        return ResponseFactory.data(responseDTO);
    }

    @PostMapping("/search")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> search(@RequestBody CushionSearchRequest searchRequest, @ParameterObject Pageable pageable) {
        ResponseDTO responseDTO = cushionService.search(searchRequest, pageable);
        return ResponseFactory.data(responseDTO);
    }
}
