package com.example.shoestore.core.sale.store.model.response;

import java.math.BigDecimal;

public interface GetVoucher
{
    BigDecimal getSalePrice();

    Integer getSalePercent();
}
