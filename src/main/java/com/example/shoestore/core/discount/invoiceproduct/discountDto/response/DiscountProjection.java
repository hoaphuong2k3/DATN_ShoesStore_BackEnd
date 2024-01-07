package com.example.shoestore.core.discount.invoiceproduct.discountDto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Projection for {@link com.example.shoestore.entity.Discount}
 */
public interface DiscountProjection {
    Long getId();

    LocalDateTime getCreatedTime();

    LocalDateTime getUpdatedTime();

    String getCreatedBy();

    String getUpdatedBy();

    String getCode();

    String getName();

    BigDecimal getSalePrice();

    Integer getSalePercent();

    Integer getQuantity();

    BigDecimal getMinPrice();

    String getDescription();

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();

    Integer getStatus();

    Boolean getIsDeleted();

    Integer getDiscountType();

    Integer getAvailable();

    Long getPromoId();

    Long getShoesDetailId();
}