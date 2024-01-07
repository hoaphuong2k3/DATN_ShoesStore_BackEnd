package com.example.shoestore.core.sale.bill.model.response;

import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface DeliveryResponse {

    Long getId();

    String getCode();

    LocalDateTime getShipDate();

    LocalDateTime getCancellationDate();

    LocalDateTime getReceivedDate();

    String getDeliveryAddress();

    String getRecipientName();

    String getRecipientPhone();

    BigDecimal getDeliveryCost();

    Integer getStatus();
}
