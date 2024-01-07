package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.product.admin.dto.request.ToeCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.ToeSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.ToeUpdateRequest;
import com.example.shoestore.core.product.admin.service.ToeService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/toe")
@RequiredArgsConstructor

public class ToeController {

    private final ToeService toeService;

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAll() {

        ResponseDTO responseDTO = toeService.getALl();

        return ResponseFactory.data(responseDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> create(@RequestBody ToeCreateRequest createRequest){

        ResponseDTO responseDTO = toeService.create(createRequest);

        return ResponseFactory.data(responseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody ToeUpdateRequest updateRequest){

        ResponseDTO responseDTO = toeService.update(id,updateRequest);

        return ResponseFactory.data(responseDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){

        ResponseDTO responseDTO = toeService.findOne(id);

        return ResponseFactory.data(responseDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deletes(@RequestBody List<Long> listId) {
        ResponseDTO responseDTO = toeService.deleteMultipart(listId);
        return ResponseFactory.data(responseDTO);
    }

    @PostMapping("/search")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> search(@RequestBody ToeSearchRequest searchRequest, @ParameterObject Pageable pageable) {
        ResponseDTO responseDTO = toeService.search(searchRequest, pageable);
        return ResponseFactory.data(responseDTO);
    }
}
