package com.example.shoestore.core.sale.store.model.response;

import java.math.BigDecimal;

public interface CartResponse {

    Long getId();

    Integer getQuantity();

    BigDecimal getTotalPrice();
    BigDecimal getDiscountPrice();

    String getSizeName();
    String getColorName();
    String getShoesName();

    String getImgUri();
}
