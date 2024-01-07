package com.example.shoestore.core.discount.invoiceproduct.discountDto.request;

import com.example.shoestore.core.common.dto.BaseDTO;
import com.example.shoestore.infrastructure.constants.MessageCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PromoRequest extends BaseDTO {

    private Long id;

    private String code;

    @NotNull(message = MessageCode.Discount.NAME_NOT_NULL)
    @NotBlank(message = MessageCode.Discount.NAME_NOT_BLANK)
    @Length(max = 255, message = MessageCode.Discount.NAME_MAX_LENGTH)
    private String name;

    private Boolean sale;

    private String salePrice;

//    @NotBlank(message = MessageCode.DiscountPeriod.SALE_PERCENT_NOT_NULL)
    private String salePercent;

    private Integer quantity;

    @NotBlank(message = MessageCode.Discount.MIN_PRICE_PROMO_NOT_NULL)
    private String minPrice;

    @NotNull(message = MessageCode.Discount.DESCRIPTION_NOT_NULL)
    @NotBlank(message = MessageCode.Discount.DESCRIPTION_NOT_BLANK)
    @Length(max = 255, message = MessageCode.Discount.DESCRIPTION_MAX_LENGTH)
    private String description;

    @NotBlank(message = MessageCode.Discount.START_DATE_NOT_NULL)
    private String startDate;

    @NotBlank(message = MessageCode.Discount.END_DATE_NOT_NULL)
    private String endDate;

    private Integer status;

    private Integer discountType;
    private List<Long> idShoe;
}
