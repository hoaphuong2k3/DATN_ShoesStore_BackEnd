package com.example.shoestore.core.product.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class ShoesExportExcelRequest {

    private Integer stt;
    private String code;
    private String name;
    private String brand;
    private String origin;
    private String designStyle;
    private String skinType;
    private String sole;
    private String lining;
    private String toe;
    private String cushion;
    private String imgName;
    private String imgURI;
    private BigDecimal priceMin;
    private BigDecimal priceMax;
    private BigDecimal discountPriceMin;
    private BigDecimal discountPriceMax;
    private Long totalQuantity;
    private Long totalRecord;
    private String createdBy;
    private LocalDateTime createdTime;
}
