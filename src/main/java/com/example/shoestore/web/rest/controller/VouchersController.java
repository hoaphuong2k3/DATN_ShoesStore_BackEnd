package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.request.DiscountRequest;
import com.example.shoestore.core.discount.invoiceproduct.voucher.service.AdminVoucherService;
import com.example.shoestore.infrastructure.exception.ValidateException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/vouchers")
public class VouchersController {

    @Autowired
    AdminVoucherService adminVouchersService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
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
        return ResponseEntity.ok(adminVouchersService.getAll(code, name, minPrice, maxPrice, fromDate, toDate, status, type, isDelete, page, size, sortField, sortOrder));
    }

    @GetMapping("/getAllIsActive")
    public ResponseEntity<Object> getAllIsActive(@RequestParam(value = "id", required = false) Long id) {
        return ResponseFactory.data(adminVouchersService.getAllIsActive(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createVoucher")
    public ResponseEntity<Object> createVouchers(@Valid @RequestBody DiscountRequest createVoucher) {
        ResponseDTO responseDTO = adminVouchersService.createVoucher(createVoucher);
        return ResponseFactory.data(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateVoucher")
    public ResponseEntity<Object> updateVouchers(@Valid @RequestBody DiscountRequest updateVoucher) {
        ResponseDTO responseDTO = adminVouchersService.updateVoucher(updateVoucher);
        return ResponseFactory.data(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/setVoucherRun/{id}")
    public ResponseEntity<Object> setVouchersRun(@PathVariable("id") Long id) throws ValidateException {
        ResponseDTO responseDTO = adminVouchersService.setVoucherRunNow(id);
        return ResponseFactory.data(responseDTO);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/stopVoucher/{id}")
    public ResponseEntity<Object> stopVouchers(@PathVariable("id") Long id) {
        ResponseDTO responseDTO = adminVouchersService.stopVoucher(id);
        return ResponseFactory.data(responseDTO);
    }

//    @PatchMapping("/restoreVoucher")
//    public ResponseEntity<Object> restoreVouchers(@RequestBody DiscountRequest restoreVoucher) {
//        ResponseDTO responseDTO = adminVouchersService.restoreVoucher(restoreVoucher);
//        return ResponseFactory.data(responseDTO);
//    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteVoucher/{id}")
    public ResponseEntity<Object> deleteVouchers(@PathVariable("id") Long voucherId) {
        System.out.println(voucherId);
        ResponseDTO responseDTO = adminVouchersService.deleteVoucher(voucherId);
        return ResponseFactory.data(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteVoucherAll")
    public ResponseEntity<Object> deleteDiscountPeriod(@RequestParam List<Long> deleteAll) {
        ResponseDTO responseDTO = adminVouchersService.deleteAllVoucher(deleteAll);
        return ResponseFactory.data(responseDTO);
    }
}
