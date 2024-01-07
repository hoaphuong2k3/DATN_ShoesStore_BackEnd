package com.example.shoestore.core.sale.bill.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ExportOrder {
    String getCode();

    String getNameAdmin();

    String getNameClient();

    BigDecimal getTotalMoney();

    BigDecimal getTotalPayment();

    Integer getPaymentMethod();

    LocalDateTime getDatePayment();

    LocalDateTime getCreatedTime();

    Boolean getSaleStatus();

    Integer getPoints();

    Integer getStatus();


}
