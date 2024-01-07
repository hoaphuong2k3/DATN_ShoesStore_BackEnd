package com.example.shoestore.core.statistics.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Projection for {@link com.example.shoestore.entity.Order}
 */
public interface OrderInfo {

    LocalDateTime getCreatedTime();

    LocalDateTime getUpdatedTime();

    String getCreatedBy();

    String getUpdatedBy();

    String getCode();

    BigDecimal getTotalMoney();

    BigDecimal getTotalPayment();

    LocalDateTime getDatePayment();

    BigDecimal getShippingPrice();

    LocalDateTime getShippingDate();

    LocalDateTime getReceivedDate();

    Integer getPaymentMethod();

    Boolean getSaleStatus();

    Long getTotalQuantity();

    BigDecimal getTotalPrice();

    Integer getStatus();

    Boolean getIsDeleted();
}