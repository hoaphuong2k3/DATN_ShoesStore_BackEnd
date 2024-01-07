package com.example.shoestore.core.product.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class ShoesDetailExportPDFRequest {

    private Integer stt;
    private String code;
    private String size;
    private String color;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Integer status;
    private String createdBy;
    private Timestamp createdTime;
}
