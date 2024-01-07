package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.product.user.dto.request.UserShoesSearchRequest;
import com.example.shoestore.core.product.user.service.UserShoesService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/shoes")
@RequiredArgsConstructor
public class UserShoesController{

    private final UserShoesService shoesService;

    @PostMapping("/search")
    public ResponseEntity<Object> search(@RequestBody UserShoesSearchRequest searchDTO, @ParameterObject Pageable pageable) {
        ResponseDTO responseDTO = shoesService.search(searchDTO, pageable);
        return ResponseFactory.data(responseDTO);
    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<Object> getOne(@PathVariable("id") Long id) {
        ResponseDTO responseDTO = shoesService.getOne(id);
        return ResponseFactory.data(responseDTO);
    }

    @GetMapping("/get-one/shoes/{shoesId}")
    public ResponseEntity<Object> getOneByShoesId(@PathVariable("shoesId") Long shoesId) {
        ResponseDTO responseDTO = shoesService.getOneByShoesId(shoesId);
        return ResponseFactory.data(responseDTO);
    }

    @GetMapping("/get-top4/selling")
    public ResponseEntity<Object> getTop4Sell() {
        ResponseDTO responseDTO = shoesService.getTop4Sell();
        return ResponseFactory.data(responseDTO);
    }

    @GetMapping("/get-top4/sell-lot")
    public ResponseEntity<Object> getTop4SellLot() {
        ResponseDTO responseDTO = shoesService.getTop4SellLot();
        return ResponseFactory.data(responseDTO);
    }

    @GetMapping("/get-all-color/{id}")
    public ResponseEntity<Object> getAllColorById(@PathVariable("id") Long id) {
        ResponseDTO responseDTO = shoesService.getAllColorById(id);
        return ResponseFactory.data(responseDTO);
    }

    @GetMapping("/get-all-size/{id}")
    public ResponseEntity<Object> getAllSizeById(@PathVariable("id") Long id) {
        ResponseDTO responseDTO = shoesService.getAllSizeById(id);
        return ResponseFactory.data(responseDTO);
    }



}
