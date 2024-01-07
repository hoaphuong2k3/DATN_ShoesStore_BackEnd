package com.example.shoestore.core.exchange.exchange.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateExchangDTO {
    private Long idOrder;

    private String exchangeReason;

    private Boolean exchangeType;

    private List<ShoesExchangeDTO> shoesExchange;
}
