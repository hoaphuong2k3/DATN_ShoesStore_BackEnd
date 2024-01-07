package com.example.shoestore.core.sale.bill.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class BillPrintRequestDTO {

    private Integer coinRefund;

    private Integer accumulatedCoins;

    private String nameClient;

    private String phoneClient;

    private String addressDelivery;

    private BigDecimal priceDiscountPeriod;
}
