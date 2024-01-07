package com.example.shoestore.core.product.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class UserShoesSearchResponse {

    private Long id;
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
    private BigDecimal priceMax;
    private BigDecimal priceMin;
    private BigDecimal discountPriceMax;
    private BigDecimal discountPriceMin;

}
