package com.example.shoestore.core.sale.cart.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponse {

    private List<ShoesCartResponse> shoesCart;

    private BigDecimal totalMoney;
    private BigDecimal totalPayment;
    private String freeGiftName;
    private byte[] freeGiftImage;
    private Long idDiscountPeriod;
    private Integer periodType;
    private Integer points;
    private Integer totalPoints;

}
