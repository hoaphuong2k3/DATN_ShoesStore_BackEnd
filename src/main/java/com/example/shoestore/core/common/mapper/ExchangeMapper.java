package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.exchange.exchange.model.request.CreateExchangDTO;
import com.example.shoestore.entity.Exchange;

public interface ExchangeMapper  {

    Exchange exchangeDTOToEntity(CreateExchangDTO createExchangDTO);
}
