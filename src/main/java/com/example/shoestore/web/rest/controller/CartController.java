package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.sale.cart.model.request.CheckoutDTO;
import com.example.shoestore.core.sale.cart.model.request.DeleteShoesCartDTO;
import com.example.shoestore.core.sale.cart.model.request.ShoesDetailDTO;
import com.example.shoestore.core.sale.cart.service.ShoesCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ShoesCartService shoesCartService;

    @PostMapping("/add")
    public ResponseEntity<Object> addToCart(@RequestBody ShoesDetailDTO shoesDetailDTO) {
        return ResponseFactory.data(shoesCartService.addToCart(shoesDetailDTO));
    }

    @DeleteMapping("/delete/{key}/{id}")
    public ResponseEntity<Object> delete(@PathVariable("key") Long key,
                                         @PathVariable("id") Long id) {
        return ResponseFactory.data(shoesCartService.removeValueInRedis(key, id));
    }

    @DeleteMapping("/delete/{key}")
    public ResponseEntity<Object> deleteList(@PathVariable("key") Long key,
                                             @RequestBody DeleteShoesCartDTO shoesCartDTO) {
        return ResponseFactory.data(shoesCartService.deleteListShoesInCart(key, shoesCartDTO));
    }

    @GetMapping("/{key}")
    public ResponseEntity<Object> findShoesInCart(@RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "5") int size,
                                                  @PathVariable("key") Long key) {
        return ResponseEntity.ok(shoesCartService.findShoesInCart(page, size, key));
    }

    @PutMapping("/update")
    public ResponseEntity<Object> changeQuantity(@RequestBody ShoesDetailDTO shoesDetailDTO) {
        return ResponseFactory.data(shoesCartService.updateQuantity(shoesDetailDTO));
    }

    @PostMapping("/checkout")
    public ResponseEntity<Object> proceedPayment(@RequestBody CheckoutDTO checkoutDTO) {
        return ResponseEntity.ok(shoesCartService.checkout(checkoutDTO));
    }

    @PostMapping("/byNow")
    public ResponseEntity<Object> byNow(@RequestBody ShoesDetailDTO shoesDetailDTO) {
        return ResponseEntity.ok(shoesCartService.buyNow(shoesDetailDTO));
    }

    @DeleteMapping("/delete-shoes-checkout")
    public ResponseEntity<Object> deleteShoesInCheckout(@RequestParam(name = "key") Long key,
                                                        @RequestParam(name = "id") Long id) {
        return ResponseEntity.ok(shoesCartService.deleteShoesInCheckout(key, id));
    }


    @GetMapping("/find-checkout/{key}")
    public ResponseEntity<Object> findProceedPayment(@PathVariable("key") Long key) {
        return ResponseEntity.ok(shoesCartService.findCheckout(key));
    }
}
