package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.FileResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.sale.bill.model.request.BillPrintRequestDTO;
import com.example.shoestore.core.sale.bill.model.request.CreateOrderDTO;
import com.example.shoestore.core.sale.bill.model.request.UpdateStatusDTO;
import com.example.shoestore.core.sale.bill.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@CrossOrigin("*")
@RequestMapping("/order")
public class UserOrderController {
    @Autowired
    private UserOrderService orderService;

    @PostMapping("/create")
    private ResponseEntity<Object> create(@RequestBody CreateOrderDTO createOrderDTO) {
        return ResponseFactory.data(orderService.createBill(createOrderDTO));
    }

    @PutMapping("/confirm")
    private ResponseEntity<Object> confirm(@RequestBody UpdateStatusDTO statusDTO) {
        return ResponseFactory.data(orderService.updateBill(statusDTO));
    }

    @GetMapping("/detail-bill/{idOrder}")
    private ResponseEntity<Object> confirm(@PathVariable("idOrder") Long idOrder) {
        return ResponseFactory.data(orderService.detailOrder(idOrder));
    }

    @GetMapping("/order-detail/{idClient}")
    private ResponseEntity<Object> findOrderDetail(@PathVariable("idClient") Long idClient,
                                                   @RequestParam(name = "status", defaultValue = "0") Integer status,
                                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                                   @RequestParam(name = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok(orderService.findOrder(idClient, status, page, size));
    }

    @PostMapping("/order-detail/print/{idOrder}")
    private ResponseEntity<Object> findOrderDetail(@PathVariable("idOrder") Long idOrder, @RequestBody BillPrintRequestDTO requestDTO) {
        FileResponseDTO fileResponseDTO = orderService.printBill(idOrder,requestDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileResponseDTO.getFileName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileResponseDTO.getByteArrayResource());
    }

}
