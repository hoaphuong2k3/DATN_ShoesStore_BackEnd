package com.example.shoestore.core.exchange.exchange.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.exchange.exchange.model.request.CreateExchangDTO;

public interface ExchangeShoesService {

    ResponseDTO create(CreateExchangDTO createExchangDTO);
}
