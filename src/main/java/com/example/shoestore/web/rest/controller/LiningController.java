package com.example.shoestore.web.rest.controller;


import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.product.admin.dto.request.LiningCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.LiningSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.LiningUpdateRequest;
import com.example.shoestore.core.product.admin.service.LiningService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/lining")
@RequiredArgsConstructor

public class LiningController {

    private final LiningService liningService;

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAll() {

        ResponseDTO responseDTO = liningService.getALl();

        return ResponseFactory.data(responseDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> create(@RequestBody LiningCreateRequest createRequest){

        ResponseDTO responseDTO = liningService.create(createRequest);

        return ResponseFactory.data(responseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody LiningUpdateRequest updateRequest){

        ResponseDTO responseDTO = liningService.update(id,updateRequest);

        return ResponseFactory.data(responseDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){

        ResponseDTO responseDTO = liningService.findOne(id);

        return ResponseFactory.data(responseDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deletes(@RequestBody List<Long> listId) {
        ResponseDTO responseDTO = liningService.deleteMultipart(listId);
        return ResponseFactory.data(responseDTO);
    }

    @PostMapping("/search")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> search(@RequestBody LiningSearchRequest searchRequest, @ParameterObject Pageable pageable) {
        ResponseDTO responseDTO = liningService.search(searchRequest, pageable);
        return ResponseFactory.data(responseDTO);
    }
}
