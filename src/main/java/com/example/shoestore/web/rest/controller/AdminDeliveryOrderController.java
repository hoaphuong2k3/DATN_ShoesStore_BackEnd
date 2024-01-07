package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.sale.delivery.model.request.CreateDeliveryOrderDTO;
import com.example.shoestore.core.sale.delivery.model.request.UpdateDeliveryOrderDTO;
import com.example.shoestore.core.sale.delivery.model.response.DeliveryOrderResponse;
import com.example.shoestore.core.sale.delivery.service.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/delivery")
public class AdminDeliveryOrderController {

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Page<DeliveryOrderResponse>> pageDelivery(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                    @RequestParam(name = "size", defaultValue = "5") int size,
                                                                    @RequestParam(name = "status", required = false) Integer status,
                                                                    @RequestParam(name = "code", required = false) String code,
                                                                    @RequestParam(name = "date", required = false) String date) {
        return ResponseEntity.ok(deliveryService.pageDelivery(page, size, code, date, status));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> create(@Valid @RequestBody CreateDeliveryOrderDTO createDeliveryOrderDTO) {
        return ResponseEntity.ok(deliveryService.createDelivery(createDeliveryOrderDTO));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> update(@Valid @RequestBody UpdateDeliveryOrderDTO updateDeliveryOrderDTO) {
        return ResponseEntity.ok(deliveryService.updateDelivery(updateDeliveryOrderDTO));
    }

    @PatchMapping("/delete/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok(deliveryService.deleteDelivery(id));
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> detail(@PathVariable("id") Long id) {
        return ResponseEntity.ok(deliveryService.detailDelivery(id));

    }

    @PutMapping("/update-status/{id}/{status}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> updateStatus(@PathVariable("id") Long id,
                                               @PathVariable("status") Integer status) {
        return ResponseEntity.ok(deliveryService.updateStatus(status, id));
    }
}
