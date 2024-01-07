package com.example.shoestore.core.product.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserShoesDetailRequest {

    private Long shoesId;

    private Long sizeId;

    private Long colorId;

}
