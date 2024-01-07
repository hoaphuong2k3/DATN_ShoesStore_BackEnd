package com.example.shoestore.core.product.admin.dto;

import com.example.shoestore.core.common.dto.BaseDTO;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShoesDetailDTO extends BaseDTO {


    private String code;

    private Long shoesId;

    private Long sizeId;

    private Long colorId;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal discountPrice;

    private Integer status;

    private String QRCodeURI;

}
