package com.example.shoestore.core.exchange.exchange.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.exchange.exchange.model.request.ConfirmExchangeDTO;

public interface AdminExchangeService {

    ResponseDTO findExhange(int page, int size );

    ResponseDTO confirm(ConfirmExchangeDTO exchangeDTO);
}
