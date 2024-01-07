package com.example.shoestore.core.product.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ShoesDetailReportDocResponse {

    private Integer stt;

    private String code;

    private String size;

    private String color;

    private BigDecimal price;

    private String createdBy;

    private Timestamp createdTime;
}
