package com.example.shoestore.core.discount.discountperiodtype.dto.response;

import com.example.shoestore.entity.DiscountPeriod;
import lombok.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


public interface DiscountPeriodResponsePrejection {

    Long getId();
    String getCode();
    String getName();
    String getCreatedBy();
    String getUpdatedBy();
    LocalDate getStartDate();
    LocalDate getEndDate();
    BigDecimal getMinPrice();
    Integer getSalePercent();
    LocalDateTime getCreatedTime();
    LocalDateTime getUpdatedTime();
    Integer getStatus();
    Boolean getIsDeleted();
    Long getGiftId();
    Integer getTypePeriod();
    String getNameImage();
}