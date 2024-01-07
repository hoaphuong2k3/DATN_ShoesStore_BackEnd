package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.sale.store.model.request.CreateOrderStoreDTO;
import com.example.shoestore.core.sale.store.service.AdminOrderStoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
public class OrderStoreController {

    @Autowired
    private AdminOrderStoreService orderStoreService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> create(@Valid @RequestBody CreateOrderStoreDTO orderStoreDTO) {
        return ResponseFactory.data(orderStoreService.createOrderStore(orderStoreDTO));
    }

    @GetMapping("/find-discount_period")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> findDiscountPeriod() {
        return ResponseFactory.data(orderStoreService.findDiscountPeriod());
    }
}
