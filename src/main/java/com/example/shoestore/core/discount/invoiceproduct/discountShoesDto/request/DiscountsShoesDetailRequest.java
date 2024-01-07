package com.example.shoestore.core.discount.invoiceproduct.discountShoesDto.request;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.example.shoestore.entity.DiscountsShoesDetail}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DiscountsShoesDetailRequest implements Serializable {
    private Long id;
    private Long promoId;
    private Long shoesDetailId;
    private BigDecimal discountPrice;
}