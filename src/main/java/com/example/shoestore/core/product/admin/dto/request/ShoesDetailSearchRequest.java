package com.example.shoestore.core.product.admin.dto.request;

import com.example.shoestore.infrastructure.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ShoesDetailSearchRequest {

    private String code;

    private Long sizeId;

    private Long colorId;

    private Integer fromQuantity;

    private Integer toQuantity;

    private BigDecimal fromPrice;

    private BigDecimal toPrice;

    private Integer status;

    private String fromDateStr;

    private String toDateStr;

    private String createdBy;

    private String qrCode;

    public Date getFromDate() {
        return DateUtils.strToDate(fromDateStr);
    }

    public Date getToDate() {
        return DateUtils.strToDate(toDateStr);
    }

    public final static Map<String, String> shoesDetailField() {

        Map<String, String> map = new HashMap<>();

        map.put("code", "shdt.code");
        map.put("size", "sz.name");
        map.put("color", "cl.name");
        map.put("quantity", "shdt.quantity");
        map.put("price", "shdt.price");
        map.put("discountPrice", "shdt.discount_price");
        map.put("status", "shdt.status");
        map.put("createdBy", "shdt.created_by");
        map.put("createdTime", "shdt.created_time");

        return Collections.unmodifiableMap(map);
    }
}
