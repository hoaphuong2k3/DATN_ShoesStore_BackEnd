package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.exchange.exchange.model.request.CreateExchangDTO;
import com.example.shoestore.core.exchange.exchange.service.ExchangeShoesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/exchange")
public class ExchangeController {
    @Autowired
    private ExchangeShoesService exchangeShoesService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody CreateExchangDTO createExchangDTO) {
        return ResponseFactory.data(exchangeShoesService.create(createExchangDTO));
    }
}
