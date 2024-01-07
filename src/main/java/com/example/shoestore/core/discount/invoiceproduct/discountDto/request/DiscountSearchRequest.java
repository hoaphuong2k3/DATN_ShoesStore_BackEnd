package com.example.shoestore.core.discount.invoiceproduct.discountDto.request;

import com.example.shoestore.infrastructure.utils.DateUtils;
import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class DiscountSearchRequest {

    private String code;
    private String name;
    private String fromStartDate;
    private String toEndDate;
    private Integer status;
    private Integer discountType;

    public Date getFromDate() {
        return DateUtils.strToDate(fromStartDate);
    }

    public Date getToDate() {
        return DateUtils.strToDate(toEndDate);
    }

    public final static Map<String, String> discountField() {

        Map<String, String> map = new HashMap<>();

        map.put("code", "ds.code");
        map.put("name", "ds.name");
        map.put("status", "ds.status");
        map.put("discountType", "ds.discount_type");
        map.put("fromStartDate", "ds.start_date");
        map.put("toEndDate", "ds.end_date");

        return Collections.unmodifiableMap(map);
    }
}
