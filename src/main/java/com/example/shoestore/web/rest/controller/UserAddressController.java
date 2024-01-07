package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.account.client.model.request.CreateAddressDTO;
import com.example.shoestore.core.account.client.model.request.UpdateAddressDTO;
import com.example.shoestore.core.account.client.service.AddressService;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
public class UserAddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> page(@PathVariable("id") Long idClient,
                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                       @RequestParam(name = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok(addressService.pageAddress(page, size, idClient));
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@Valid @RequestBody CreateAddressDTO addressDTO) {
        return ResponseFactory.data(addressService.create(addressDTO));
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(@Valid @RequestBody UpdateAddressDTO addressDTO) {
        return ResponseFactory.data(addressService.update(addressDTO));
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        return ResponseFactory.data(addressService.delete(id));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Object> detail(@PathVariable("id") Long id) {
        return ResponseFactory.data(addressService.detail(id));
    }


}
