package com.example.shoestore.core.sale.cart.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoesDetailDTO {

    private Long key;
    private Long id; // idShoesDetail
    private Integer quantity;

}
