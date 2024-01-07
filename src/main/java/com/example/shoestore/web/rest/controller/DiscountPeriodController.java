package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.discount.discountperiodtype.dto.request.DiscountPeriodRequest;
import com.example.shoestore.core.discount.discountperiodtype.service.AdminDiscoutPeriodService;
import com.example.shoestore.infrastructure.exception.ValidateException;
import jakarta.validation.Valid;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin/discount-period")
public class DiscountPeriodController {

    @Autowired
    AdminDiscoutPeriodService adminDiscoutPeriodService;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<Object> getAll(@RequestParam(value = "code", defaultValue = "") String code,
                                         @RequestParam(value = "name", defaultValue = "") String name,
                                         @RequestParam(value = "minPercent", required = false) Integer minPercent,
                                         @RequestParam(value = "maxPercent", required = false) Integer maxPercent,
                                         @RequestParam(value = "fromDate", required = false) LocalDate fromDate,
                                         @RequestParam(value = "toDate", required = false) LocalDate toDate,
                                         @RequestParam(value = "status", required = false) Integer status,
                                         @RequestParam(value = "isdelete", required = false) Integer isDelete,
                                         @RequestParam(value = "typePeriod", required = false) Integer typePeriod,
                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                         @RequestParam(name = "size", defaultValue = "10") int size,
                                         @RequestParam(name = "sortField", required = false) String sortField,
                                         @RequestParam(name = "sortOrder", required = false) String sortOrder) throws ValidateException {
        return ResponseEntity.ok(adminDiscoutPeriodService.search(code, name, minPercent, maxPercent, fromDate, toDate, status, isDelete, typePeriod, page, size, sortField, sortOrder));
    }

    @GetMapping("getAllIsActive")
    public ResponseEntity<Object> getAllIsActive() {
        return ResponseFactory.data(adminDiscoutPeriodService.getAllIsActive());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createDiscountPeriod")
    public ResponseEntity<Object> createDiscountPeriod(@Valid @RequestBody DiscountPeriodRequest createDiscountPeriodRequest) {
        ResponseDTO responseDTO = adminDiscoutPeriodService.createDiscountPeriod(createDiscountPeriodRequest);
        return ResponseFactory.data(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateDiscountPeriod")
    public ResponseEntity<Object> updateDiscountPeriod(@Valid @RequestBody DiscountPeriodRequest updateDiscountPeriodRequest) {
        ResponseDTO responseDTO = adminDiscoutPeriodService.updateDiscountPeriod(updateDiscountPeriodRequest);
        return ResponseFactory.data(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/stopDiscountPeriod/{id}")
    public ResponseEntity<Object> stopDiscountPeriod(@PathVariable("id") Long id) {
        ResponseDTO responseDTO = adminDiscoutPeriodService.stopDiscountPeriod(id);
        return ResponseFactory.data(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/setDiscountPeriodRun/{id}")
    public ResponseEntity<Object> setDiscountPeriodRun(@PathVariable("id") Long id) {
        ResponseDTO responseDTO = adminDiscoutPeriodService.setDiscountPeriodRunNow(id);
        return ResponseFactory.data(responseDTO);
    }

//    @PatchMapping("/restoreDiscountPeriod")
//    public ResponseEntity<Object> restoreDiscountPeriod(@RequestBody DiscountPeriodRequest restoreDiscountPeriod) {
//        ResponseDTO responseDTO = adminDiscoutPeriodService.restoreDiscountPeriod(restoreDiscountPeriod);
//        return ResponseFactory.data(responseDTO);
//    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteDiscountPeriod/{id}")
    public ResponseEntity<Object> deleteDiscountPeriod(@PathVariable("id") Long id) {
        ResponseDTO responseDTO = adminDiscoutPeriodService.deleteDiscountPeriod(id);
        return ResponseFactory.data(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteDiscountPeriodAll")
    public ResponseEntity<Object> deleteDiscountPeriod(@RequestParam List<Long> deleteAll) {
        ResponseDTO responseDTO = adminDiscoutPeriodService.deleteAll(deleteAll);
        return ResponseFactory.data(responseDTO);
    }
}
