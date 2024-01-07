package com.example.shoestore.core.exchange.exchange.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmExchangeDTO {
    private Long idOrder;

    private Long idExchange;

    private Integer statusOrder;

    private Integer statusExchange;


}
