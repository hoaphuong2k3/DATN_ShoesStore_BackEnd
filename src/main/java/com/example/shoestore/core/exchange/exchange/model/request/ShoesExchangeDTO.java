package com.example.shoestore.core.exchange.exchange.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoesExchangeDTO {
    private Long idShoesDetail;
    private Integer quantity;
    private Boolean isConfirm;
}
