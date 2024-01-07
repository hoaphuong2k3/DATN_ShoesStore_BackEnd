package com.example.shoestore.core.sale.store.model.request;

import com.example.shoestore.infrastructure.constants.MessageCode;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateShoesInCart {
    @Size(min = 1, max = 30, message = MessageCode.Cart.QUANTITY_MAX_15)
    private Integer quantity;
    private Long idShoesDetail;
}
