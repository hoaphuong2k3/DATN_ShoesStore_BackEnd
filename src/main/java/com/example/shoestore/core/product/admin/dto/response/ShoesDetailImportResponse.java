package com.example.shoestore.core.product.admin.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class ShoesDetailImportResponse {

    private Integer stt;

    private Long shoesId;

    private Long sizeId;

    private Long colorId;

    private BigDecimal price;

    private Integer quantity;

    private Integer status;

    private String error;
}
