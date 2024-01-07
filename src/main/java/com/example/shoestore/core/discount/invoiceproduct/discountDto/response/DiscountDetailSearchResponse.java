package com.example.shoestore.core.discount.invoiceproduct.discountDto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDetailSearchResponse {

    private DiscountSearchResponse discountSearchResponse;

    private List<Long> iddDiscountShoe;
}
