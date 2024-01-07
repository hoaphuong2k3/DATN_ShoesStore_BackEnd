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

public class ShoesSearchRequest {

    private String codeOrName;
    private Long brandId;
    private Long originId;
    private Long designStyleId;
    private Long skinTypeId;
    private Long soleId;
    private Long liningId;
    private Long toeId;
    private Long cushionId;

    private BigDecimal fromPrice;
    private BigDecimal toPrice;

    private Integer fromQuantity;
    private Integer toQuantity;

    private String fromDateStr;
    private String toDateStr;

    private String createdBy;

    public Date getFromDate() {
        return DateUtils.strToDate(fromDateStr);
    }

    public Date getToDate() {
        return DateUtils.strToDate(toDateStr);
    }

    public final static Map<String, String> shoesField() {

        Map<String, String> map = new HashMap<>();

        map.put("code", "sh.code");
        map.put("name", "sh.name");
        map.put("brand", "br.name");
        map.put("origin", "o.name");
        map.put("designStyle", "dsst.name");
        map.put("skinType", "skt.name");
        map.put("sole", "sl.name");
        map.put("lining", "ln.name");
        map.put("toe", "t.name");
        map.put("cushion", "cs.name");
        map.put("price", "shdt.code");
        map.put("priceMin", "priceMin");
        map.put("priceMax", "priceMax");
        map.put("discountPriceMin", "discountPriceMin");
        map.put("discountPriceMax", "discountPriceMax");
        map.put("totalQuantity", "totalQuantity");
        map.put("totalRecord", "totalRecord");
        map.put("createdBy", "sh.created_by");
        map.put("createdTime", "sh.created_time");

        return Collections.unmodifiableMap(map);
    }

}
