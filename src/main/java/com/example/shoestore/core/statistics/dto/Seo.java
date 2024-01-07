package com.example.shoestore.core.statistics.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface Seo {

    BigDecimal getTotalMoney();

    BigDecimal getTotalPayment();

    Integer getTotalQuantity();

    String getCreatedTime();

    BigDecimal getTotalPrice();
}
