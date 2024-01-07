package com.example.shoestore.core.discount.discountperiodtype.dto.response;

import com.example.shoestore.core.common.dto.BaseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DiscountPeriodResponse extends BaseDTO {

    private String name;
    private String code;
    private Integer salePercent;
    private Integer status;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal minPrice;
    private Long giftId;
    private Integer typePeriod;
}
