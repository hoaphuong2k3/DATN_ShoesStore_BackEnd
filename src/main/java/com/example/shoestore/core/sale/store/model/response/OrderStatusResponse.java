package com.example.shoestore.core.sale.store.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderStatusResponse {

    Long getId();
    Long getClientId();

    String getCode();

    BigDecimal getTotalMoney();

    Integer getPaymentMethod();

    LocalDateTime getCreatedTime();

    LocalDateTime getUpdatedTime();

    Integer getStatus();

    String getCreatedBy();

    String getUpdateBy();

    Boolean getIsDeleted();

    BigDecimal getPriceVoucher();

    Integer getPercentVoucher();

    Integer getPercentPeriod();


}
