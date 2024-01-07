package com.example.shoestore.core.sale.cart.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteShoesCartDTO {
    private List<Long> id;
}
