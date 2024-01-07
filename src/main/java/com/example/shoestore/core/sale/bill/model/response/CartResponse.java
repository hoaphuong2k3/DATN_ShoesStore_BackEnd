package com.example.shoestore.core.sale.bill.model.response;

import java.math.BigDecimal;

public interface CartResponse {

    Integer getStt();

    Long getId();
    Long getIdShoesDetail();

    Integer getQuantity();

    BigDecimal getTotalPrice();

    BigDecimal getDiscountPrice();

    BigDecimal getPrice();

    Long getIdSize();

    String getSizeName();

    Long getIdColor();

    String getColorName();

    Long getIdShoes();

    String getShoesName();

    String getImgUri();
}
