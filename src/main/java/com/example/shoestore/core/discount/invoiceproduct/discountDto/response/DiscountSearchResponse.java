package com.example.shoestore.core.discount.invoiceproduct.discountDto.response;

import com.example.shoestore.core.common.dto.BaseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DiscountSearchResponse extends BaseDTO {

    private Long id;

    private String code;

    private String name;

    private BigDecimal salePrice;

    private Integer salePercent;

    private Integer quantity;

    private BigDecimal minPrice;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Boolean isDeleted;

    private Integer status;

    private Integer discountType;

    private Integer available;
}
