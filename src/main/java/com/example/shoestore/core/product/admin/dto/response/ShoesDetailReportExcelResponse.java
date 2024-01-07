package com.example.shoestore.core.product.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class ShoesDetailReportExcelResponse {

    private Integer stt;

    private String shoesCode;

    private String code;

    private String size;

    private String color;

    private BigDecimal price;

    private String createdBy;

    private LocalDateTime createdTime;

    private String updatedBy;

    private LocalDateTime updatedTime;
}
