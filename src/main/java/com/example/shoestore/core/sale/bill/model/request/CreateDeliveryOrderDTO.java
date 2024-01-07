package com.example.shoestore.core.sale.bill.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeliveryOrderDTO {
    private String address;

    private String recipientName;

    private String recipientPhone;

    private BigDecimal deliveryCost;



}
