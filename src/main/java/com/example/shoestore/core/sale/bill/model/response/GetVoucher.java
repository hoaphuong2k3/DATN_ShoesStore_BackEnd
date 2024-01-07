package com.example.shoestore.core.sale.bill.model.response;

import java.math.BigDecimal;

public interface GetVoucher
{
    BigDecimal getSalePrice();

    Integer getSalePercent();
}
