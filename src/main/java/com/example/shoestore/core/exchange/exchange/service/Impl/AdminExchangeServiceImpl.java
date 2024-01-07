package com.example.shoestore.core.exchange.exchange.service.Impl;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.exchange.exchange.model.request.ConfirmExchangeDTO;
import com.example.shoestore.core.exchange.exchange.repository.AdminExchangeRepository;
import com.example.shoestore.core.exchange.exchange.service.AdminExchangeService;
import com.example.shoestore.core.sale.bill.service.AdminOrderService;
import com.example.shoestore.infrastructure.constants.ExchangeStatus;
import com.example.shoestore.infrastructure.constants.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminExchangeServiceImpl implements AdminExchangeService {

    @Autowired
    private AdminExchangeRepository exchangeRepository;

    @Autowired
    private AdminOrderService orderService;

    @Override
    public ResponseDTO findExhange(int page, int size) {
        return null;
    }

    @Override
    public ResponseDTO confirm(ConfirmExchangeDTO exchangeDTO) {
        if (exchangeDTO.getStatusExchange() == ExchangeStatus.WAIT_CONFIRM.getValue()) {
            exchangeRepository.updateStatus(ExchangeStatus.WAIT_CONFIRM.getValue(), exchangeDTO.getIdExchange());
//            orderService.updateStatus(OrderStatus.WAITING_EXCHANGE.getValue(), idStaff, exchangeDTO.getIdOrder());
        }

        exchangeRepository.updateStatus(ExchangeStatus.CANCEL.getValue(), exchangeDTO.getIdExchange());

        return ResponseDTO.success();
    }
}
