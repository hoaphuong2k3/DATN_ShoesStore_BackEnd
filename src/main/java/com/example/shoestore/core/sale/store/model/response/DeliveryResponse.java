package com.example.shoestore.core.sale.store.model.response;

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
    String getDeliveryCost();
    Integer getStatus();
}
