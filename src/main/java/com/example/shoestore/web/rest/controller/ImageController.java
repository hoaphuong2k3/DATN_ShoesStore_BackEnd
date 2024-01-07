package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.product.admin.service.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload-multipart/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> uploadMultipart(@PathVariable("id") Long id,@Valid @RequestParam("files") List<MultipartFile> files) {
        ResponseDTO responseDTO = imageService.uploadMultipart(id, files);
        return ResponseFactory.data(responseDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id) {

        ResponseDTO responseDTO = imageService.findOne(id);
        return ResponseFactory.data(responseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> update(@PathVariable("id") Long id,@RequestParam("file") MultipartFile file) {
        ResponseDTO responseDTO = imageService.update(id,file);
        return ResponseFactory.data(responseDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deletes(@RequestBody List<Long> listId) {
        ResponseDTO responseDTO = imageService.deleteMultipart(listId);
        return ResponseFactory.data(responseDTO);
    }

    @GetMapping("/dowload/{idShoesDetail}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> dowload(@PathVariable("idShoesDetail") Long idShoesDetail) {
        ResponseDTO responseDTO = imageService.downloadImage(idShoesDetail);
        return ResponseFactory.data(responseDTO);
    }
}
