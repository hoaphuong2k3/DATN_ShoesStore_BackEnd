package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.discount.freegift.model.request.CreateFreeGiftDTO;
import com.example.shoestore.core.discount.freegift.model.request.UpdateFreeGiftDTO;
import com.example.shoestore.core.discount.freegift.service.FreeGiftService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/free-gift")
public class AdminFreeGiftController {

    @Autowired
    private FreeGiftService freeGiftService;

    @GetMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> pageFreeGift(@RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok(freeGiftService.pageFreeGift(page, size));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> create(@Valid @RequestBody CreateFreeGiftDTO freeGiftDTO) {
        return ResponseFactory.data(freeGiftService.create(freeGiftDTO));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> update(@Valid @RequestBody UpdateFreeGiftDTO freeGiftDTO) {
        return ResponseFactory.data(freeGiftService.update(freeGiftDTO));
    }

    @PatchMapping("/delete/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        return ResponseFactory.data(freeGiftService.delete(id));
    }

    @PatchMapping("/detail/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> detail(@PathVariable("id") Long id) {
        return ResponseFactory.data(freeGiftService.detail(id));
    }

    @PostMapping("/image/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> image(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
        return ResponseFactory.data(freeGiftService.createImage(file, id));
    }


}
