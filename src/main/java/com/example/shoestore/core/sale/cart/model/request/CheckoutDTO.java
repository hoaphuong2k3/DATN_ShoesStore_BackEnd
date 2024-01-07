package com.example.shoestore.core.sale.cart.model.request;

import com.example.shoestore.core.sale.cart.model.entity.ShoesCart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutDTO {

    private Long key;

    private List<Long> listShoesCart;
}
