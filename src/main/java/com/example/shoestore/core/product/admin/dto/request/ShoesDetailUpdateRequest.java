package com.example.shoestore.core.product.admin.dto.request;

import com.example.shoestore.infrastructure.constants.MessageCode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ShoesDetailUpdateRequest {

    @NotNull(message = MessageCode.ShoesDetail.SIZE_ID_NOT_NULL)
    private Long sizeId;

    @NotNull(message = MessageCode.ShoesDetail.COLOR_ID_NOT_NULL)
    private Long colorId;

    @NotNull(message = MessageCode.ShoesDetail.QUANTITY_NOT_NULL)
    @Min(value = 1,message = MessageCode.ShoesDetail.QUANTITY_MIN)
    @Max(value = 1000,message = MessageCode.ShoesDetail.QUANTITY_MAX)
    private Integer quantity;

    @NotNull(message = MessageCode.ShoesDetail.PRICE_NOT_NULL)
    @Min(value = 1000000,message = MessageCode.ShoesDetail.PRICE_MIN)
    @Max(value = 10000000,message = MessageCode.ShoesDetail.PRICE_MAX)
    private BigDecimal price;

}
