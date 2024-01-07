package com.example.shoestore.core.discount.discountperiodtype.dto.request;


import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DiscountPeriodSearch {

    private String name;
    private String code;
    private String salePercent;
    private String startDate;
    private String endDate;
    private Integer status;
    private BigDecimal minPrice;
    private Long giftId;
    private Integer typePeriod;
    private String image;
}
