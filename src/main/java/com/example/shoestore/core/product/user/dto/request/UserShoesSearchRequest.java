package com.example.shoestore.core.product.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserShoesSearchRequest {

    // dataInput: code,name,designStyle,brand,size,color,skinType,sole,lining

    private String dataInput;

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

}
