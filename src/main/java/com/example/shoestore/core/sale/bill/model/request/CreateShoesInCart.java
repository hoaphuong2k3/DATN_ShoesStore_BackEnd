package com.example.shoestore.core.sale.bill.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateShoesInCart {
    private Integer quantity;
    private Long idShoesDetail;
    private Long idOrder;
}
