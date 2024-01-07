package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.request.DiscountRequest;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.request.PromoRequest;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.request.UpdatePromoRequest;
import com.example.shoestore.core.discount.invoiceproduct.promo.service.AdminPromosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/promos")
public class PromosController {

    @Autowired
    private AdminPromosService adminPromosService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<Object> getAll(@RequestParam(value = "code", defaultValue = "") String code,
                                         @RequestParam(value = "name", defaultValue = "") String name,
                                         @RequestParam(value = "minPrice", required = false) Integer minPrice,
                                         @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
                                         @RequestParam(value = "fromDate", required = false) LocalDate fromDate,
                                         @RequestParam(value = "toDate", required = false) LocalDate toDate,
                                         @RequestParam(value = "status", required = false) Integer status,
                                         @RequestParam(value = "type", required = false) Integer type,
                                         @RequestParam(value = "isdelete", required = false) Integer isDelete,
                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                         @RequestParam(name = "size", defaultValue = "10") int size,
                                         @RequestParam(name = "sortField", required = false) String sortField,
                                         @RequestParam(name = "sortOrder", required = false) String sortOrder) {
        return ResponseEntity.ok(adminPromosService.search(code, name, minPrice, maxPrice, fromDate, toDate, status, type, isDelete, page, size, sortField, sortOrder));
    }

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAll(){
        return ResponseFactory.data(adminPromosService.getAllIsActive());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/detailPromo/{id}")
    public ResponseEntity<Object> getAll(@PathVariable("id") Long id){
        return ResponseFactory.data(adminPromosService.detailPromos(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createPromos")
    public ResponseEntity<Object> createPromos(@Valid @RequestBody PromoRequest createPromo){
        ResponseDTO responseDTO = adminPromosService.createPromos(createPromo);
        return ResponseFactory.data(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updatePromos")
    public ResponseEntity<Object> updatePromos(@Valid @RequestBody PromoRequest updatePromo){
        ResponseDTO responseDTO = adminPromosService.updatePromos(updatePromo);
        return ResponseFactory.data(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/setPromoRun/{id}")
    public ResponseEntity<Object> setVouchersRun(@PathVariable("id") Long id) {
        ResponseDTO responseDTO = adminPromosService.setPromoRunNow(id);
        return ResponseFactory.data(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/stopPromos/{id}")
    public ResponseEntity<Object> stopPromos(@PathVariable("id") Long id){
        ResponseDTO responseDTO = adminPromosService.stopPromos(id);
        return ResponseFactory.data(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/restorePromos")
    public ResponseEntity<Object> restorePromos(@RequestBody DiscountRequest restorePromo){
        ResponseDTO responseDTO = adminPromosService.restorePromos(restorePromo);
        return ResponseFactory.data(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deletePromos/{id}")
    public ResponseEntity<Object> deletePromos(@PathVariable("id") Long promoId){
        System.out.println(promoId);
        ResponseDTO responseDTO = adminPromosService.deletePromos(promoId);
        return ResponseFactory.data(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deletePromoAll")
    public ResponseEntity<Object> deleteDiscountPeriod(@RequestParam List<Long> deleteAll) {
        ResponseDTO responseDTO = adminPromosService.deleteAllPromo(deleteAll);
        return ResponseFactory.data(responseDTO);
    }
}
