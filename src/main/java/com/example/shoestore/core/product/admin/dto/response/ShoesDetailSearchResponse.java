package com.example.shoestore.core.product.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ShoesDetailSearchResponse {

    private Long id;
    private String shoesName;
    private String code;
    private String size;
    private String color;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Integer status;
    private String createdBy;
    private LocalDateTime createdTime;
    private String QRCodeURI;

}
