package com.example.shoestore.core.sale.bill.model.response;

import java.math.BigDecimal;

public interface DiscountPeriodsResponse {
    Long getId();
    Integer getSalePercent();

    BigDecimal getMinPrice();

    Integer getStatus();

    Boolean getIsDeleted();

    Integer getTypePeriod();

    String getFreeGiftName();

    byte[] getFreeGiftImage();
}
