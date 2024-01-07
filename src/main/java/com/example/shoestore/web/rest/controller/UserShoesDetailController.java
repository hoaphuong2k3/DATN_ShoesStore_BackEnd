package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.product.user.dto.request.UserShoesDetailRequest;
import com.example.shoestore.core.product.user.service.UserShoesDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/shoesdetail")
@RequiredArgsConstructor
public class UserShoesDetailController {

    private final UserShoesDetailService shoesDetailService;

    @PostMapping("/find-one")
    public ResponseEntity<Object> findOne(@RequestBody UserShoesDetailRequest detailDTO) {

        ResponseDTO responseDTO = shoesDetailService.getOne(detailDTO);

        return ResponseFactory.data(responseDTO);
    }

    @GetMapping("/find-one/qr/{qrCode}")
    public ResponseEntity<Object> findOneByQRCode(@PathVariable("qrCode") String qrCode) {

        ResponseDTO responseDTO = shoesDetailService.getOneByQRCode(qrCode);

        return ResponseFactory.data(responseDTO);
    }

    @GetMapping("/find-one/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id) {

        ResponseDTO responseDTO = shoesDetailService.findOne(id);

        return ResponseFactory.data(responseDTO);
    }

}
