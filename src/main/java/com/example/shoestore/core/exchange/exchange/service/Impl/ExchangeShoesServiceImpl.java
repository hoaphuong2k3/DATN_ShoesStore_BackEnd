package com.example.shoestore.core.exchange.exchange.service.Impl;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.ExchangeMapper;
import com.example.shoestore.core.exchange.exchange.model.request.CreateExchangDTO;
import com.example.shoestore.core.exchange.exchange.model.request.ShoesExchangeDTO;
import com.example.shoestore.core.exchange.exchange.repository.ExchangeShoesRepository;
import com.example.shoestore.core.exchange.exchange.service.ExchangeShoesService;
import com.example.shoestore.entity.Exchange;
import com.example.shoestore.infrastructure.constants.ExchangeStatus;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.constants.OrderStatus;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.MessageUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExchangeShoesServiceImpl implements ExchangeShoesService {
    @Autowired
    private ExchangeShoesRepository exchangeRepository;

    @Autowired
    private ExchangeMapper mapper;

    private static Exchange exchange = new Exchange();

    @Override
    public ResponseDTO create(CreateExchangDTO createExchangDTO) {
        CreateExchangDTO exchangeDTO = this.validate(createExchangDTO);
        exchange = mapper.exchangeDTOToEntity(exchangeDTO);
        exchange.setStatus(ExchangeStatus.WAIT_CONFIRM.getValue());
        exchange.setExchangeDate(LocalDateTime.now());
        exchangeRepository.save(exchange);
        return ResponseDTO.success(exchangeRepository.save(exchange));
    }

    @SneakyThrows
    private CreateExchangDTO validate(CreateExchangDTO exchangeDTO) {
        List<ShoesExchangeDTO> list = exchangeDTO.getShoesExchange();
        if (exchangeDTO == null || exchangeDTO.getIdOrder() == null) {
            return null;
        }
        List<Long> idShoesDetailList = list.stream()
                .filter(item -> item.getIdShoesDetail() != null)
                .map(ShoesExchangeDTO::getIdShoesDetail)
                .collect(Collectors.toList());

        int exists = exchangeRepository.existsByOrder(exchangeDTO.getIdOrder(),idShoesDetailList);
//        int quantity = exchangeRepository.getQuantity(exchangeDTO.getIdOrder(), exchangeDTO.getIdShoesDetail());
        int status = exchangeRepository.getStatusInBill(exchangeDTO.getIdOrder());
        CreateExchangDTO exchange = new CreateExchangDTO();
        if (exists == 1) {
            if (status == OrderStatus.RECEIVED.getValue()) {
                exchange.setIdOrder(exchangeDTO.getIdOrder());
                exchange.setExchangeReason(exchangeDTO.getExchangeReason());
                exchange.setExchangeType(exchangeDTO.getExchangeType());
//                if (exchangeDTO.getQuantity() <= quantity) {
//                    exchange.setQuantity(exchangeDTO.getQuantity());
//                } else {
//                    throw new ValidateException(MessageUtils.getMessage(MessageCode.Exchange.QUANTITY_EXCHANGE));
//                }
                return exchange;
            } else {
                throw new ValidateException(MessageUtils.getMessage(MessageCode.Exchange.BILL_RECEIVED));
            }
        }
        throw new ValidateException(MessageUtils.getMessage(MessageCode.Exchange.SHOES_NOT_EXISTS));


    }
}
