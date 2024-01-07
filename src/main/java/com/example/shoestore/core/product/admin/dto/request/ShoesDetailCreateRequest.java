package com.example.shoestore.core.product.admin.dto.request;

import com.example.shoestore.infrastructure.constants.MessageCode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ShoesDetailCreateRequest {

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

    @NotNull(message = MessageCode.ShoesDetail.STATUS_NOT_NULL)
    @Min(value = 0,message = MessageCode.ShoesDetail.STATUS)
    @Max(value = 1,message = MessageCode.ShoesDetail.STATUS)
    private Integer status;
}
