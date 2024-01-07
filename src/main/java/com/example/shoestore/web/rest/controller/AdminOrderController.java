package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.FileResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.sale.bill.model.request.CreateShoesInCart;
import com.example.shoestore.core.sale.bill.model.request.UpdateDeliveryOrderDTO;
import com.example.shoestore.core.sale.bill.model.request.UpdateShoesInCart;
import com.example.shoestore.core.sale.bill.service.AdminOrderService;
import com.example.shoestore.infrastructure.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@CrossOrigin("*")
@RequestMapping("/order/admin")
public class AdminOrderController {

    @Autowired
    private AdminOrderService adminOrderService;

    @GetMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> findAll(@RequestParam(name = "status", defaultValue = "0") int status,
                                          @RequestParam(name = "code", required = false) String code,
                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                          @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(adminOrderService.findAll(status, code, page, size));
    }


    @PutMapping("/update-status/{idOrder}/{idStaff}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> updateStausOrder(@PathVariable("idOrder") Long idOrder,
                                                   @PathVariable("idStaff") Long idStaff,
                                                   @RequestParam(name = "status") Integer status) {
        return ResponseFactory.data(adminOrderService.updateStatus(status, idStaff, idOrder));
    }

    @GetMapping("/export")
//    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ByteArrayResource> exportExcel(@RequestParam(name = "date", required = false) String date,
                                                         @RequestParam(name = "status", defaultValue = "0") Integer status) {

//        if (date == null) {
//            date = DateUtils.localDateToStr(LocalDate.now());
//        }

        FileResponseDTO fileResponseDTO = adminOrderService.exportExcel(date, status);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileResponseDTO.getFileName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileResponseDTO.getByteArrayResource());
    }

    @GetMapping("/cart/get-all/{idOrder}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> listCart(@PathVariable("idOrder") Long idOrder) {
        return ResponseEntity.ok(adminOrderService.listCart(idOrder));
    }

    @GetMapping("/delivery/{idOrder}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> deliveryDetail(@PathVariable("idOrder") Long idOrder) {
        return ResponseFactory.data(adminOrderService.deliveryDetail(idOrder));
    }

    @PutMapping("/delivery/update")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> updateDelivery(@RequestBody UpdateDeliveryOrderDTO deliveryOrderDTO) {
        return ResponseFactory.data(adminOrderService.updateDeliveryOrder(deliveryOrderDTO));
    }

    @PutMapping("/delivery/change-status/{idDelivery}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> changeStatus(@PathVariable("idDelivery") Long idDelivery,
                                               @RequestParam(name = "status") Integer status) {
        return ResponseFactory.data(adminOrderService.changeStatusDeliveryOrder(idDelivery, status));
    }

    @PutMapping("/cart/update")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> updateCart(@RequestBody UpdateShoesInCart shoesInCart) {
        return ResponseFactory.data(adminOrderService.updateShoesInCart(shoesInCart));
    }

    @DeleteMapping("/cart/delete/{idShoesInCart}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> deleteCart(@PathVariable("idShoesInCart") Long idShoesInCart) {
        return ResponseFactory.data(adminOrderService.deleteShoesInCart(idShoesInCart));
    }

    @PostMapping("/cart/create")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> createCart(@RequestBody CreateShoesInCart shoesInCart) {
        return ResponseFactory.data(adminOrderService.createShoesInCart(shoesInCart));
    }

    @PutMapping("/update/total-money/{idOrder}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> updateTotalMoney(@PathVariable("idOrder") Long idOrder,
                                                   @RequestParam("total-money") BigDecimal totalMoney,
                                                   @RequestParam("total-payment") BigDecimal totalPayment) {
        return ResponseFactory.data(adminOrderService.updateTotalmoney(totalMoney, totalPayment, idOrder));
    }

    @GetMapping("/countStatus")
    public ResponseEntity<Object> countStatus(@RequestParam("status") Integer status) {
        return ResponseFactory.data(adminOrderService.countStatus(status));
    }

}
