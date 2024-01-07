package com.example.shoestore.core.sale.cart.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoesCartResponse {
    private Long id;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private String code;
    private String name;
    private String size;
    private String color;

    private String image;
}
