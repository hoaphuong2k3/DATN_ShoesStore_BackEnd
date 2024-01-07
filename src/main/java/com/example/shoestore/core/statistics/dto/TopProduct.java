package com.example.shoestore.core.statistics.dto;

import java.math.BigDecimal;

public interface TopProduct {

    Long getId();

    String getName();

    BigDecimal getTotalMoney();

    BigDecimal getTotalPayment();

    Integer getTotalQuantity();

    BigDecimal getTotalPrice();

}
