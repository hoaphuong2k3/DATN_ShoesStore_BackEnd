package com.example.shoestore.core.sale.delivery.model.response;

import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface DeliveryOrderResponse {

    Long getId();

    String getCodeDelivery();

    LocalDateTime getShipDate();

    LocalDateTime getCancellationDate();

    LocalDateTime getReceivedDate();

    String getDeliveryAddress();

    String getRecipientName();

    String getRecipientPhone();

    BigDecimal getDeliveryCost();

    Integer getStatus();

    Boolean getIsDeleted();

    Long getIdOrder();

    LocalDateTime getCreatedTime();

    LocalDateTime getUpdatedTime();
}
