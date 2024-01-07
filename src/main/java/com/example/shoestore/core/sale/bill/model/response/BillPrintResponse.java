package com.example.shoestore.core.sale.bill.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillPrintResponse {


    private String code;

    private BigDecimal totalMoney;

    private BigDecimal totalPayment;
    private BigDecimal priceShip;
    private BigDecimal priceDiscountPeriod;
    private String paymentMethod;

    private String addressDelivery;

    private List<CartResponse> listCart;


}
