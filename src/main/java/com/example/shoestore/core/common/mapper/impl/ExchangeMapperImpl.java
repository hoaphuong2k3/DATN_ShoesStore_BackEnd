package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.ExchangeMapper;
import com.example.shoestore.core.exchange.exchange.model.request.CreateExchangDTO;
import com.example.shoestore.entity.Exchange;
import org.springframework.stereotype.Service;

@Service
public class ExchangeMapperImpl implements ExchangeMapper {
    @Override
    public Exchange exchangeDTOToEntity(CreateExchangDTO createExchangDTO) {
        if (createExchangDTO == null) {
            return null;
        }
        Exchange exchange = new Exchange();
        exchange.setExchangeReason(createExchangDTO.getExchangeReason());
        exchange.setExchangeType(createExchangDTO.getExchangeType());
        exchange.setIdOrder(createExchangDTO.getIdOrder());
        return exchange;

    }
}
