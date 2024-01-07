package com.example.shoestore.core.discount.freegift.model.response;


import java.time.LocalDateTime;

public interface FreeGiftResponse {
    Long getId();

    String getCode();

    String getName();

    byte[] getImage();

    Integer getQuantity();

    Integer getStatus();

    Boolean getIsDeleted();

    LocalDateTime getCreatedTime();

    LocalDateTime getUpdatedTime();

    String getCreatedBy();

    String getUpdatedBy();


}
