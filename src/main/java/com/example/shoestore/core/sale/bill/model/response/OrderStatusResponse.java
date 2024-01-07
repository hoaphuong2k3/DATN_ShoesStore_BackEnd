package com.example.shoestore.core.sale.bill.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderStatusResponse {

    Long getId();

    Long getClientId();

    String getCode();

    BigDecimal getTotalPayment();
    BigDecimal getTotalMoney();

    Integer getPaymentMethod();

    LocalDateTime getCreatedTime();

    LocalDateTime getUpdatedTime();

    Integer getStatus();

    String getCreatedBy();

    String getUpdatedBy();

    Boolean getIsDeleted();

    BigDecimal getPriceVoucher();

    Integer getPercentVoucher();

    Integer getPercentPeriod();

    String getFullnameClient();

    String getFullnameStaff();

    String getPhoneNumber();

    String getNameFreeGift();

    byte[] getImageFreeGift();

}
