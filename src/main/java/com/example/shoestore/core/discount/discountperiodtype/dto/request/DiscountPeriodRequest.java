package com.example.shoestore.core.discount.discountperiodtype.dto.request;

import com.example.shoestore.core.common.dto.BaseDTO;
import com.example.shoestore.infrastructure.constants.MessageCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.example.shoestore.entity.DiscountPeriod}
 */
@Value
@ToString
public class DiscountPeriodRequest extends BaseDTO implements Serializable {

    @NotNull(message = MessageCode.DiscountPeriod.NAME_NOT_NULL)
    @NotBlank(message = MessageCode.DiscountPeriod.NAME_NOT_BLANK)
    @Length(max = 255, message = MessageCode.DiscountPeriod.NAME_MAX_LENGTH)
    private String name;

    private String code;

    private String salePercent;

    @NotEmpty(message = MessageCode.DiscountPeriod.START_DATE_NOT_NULL)
    private String startDate;

    @NotEmpty(message = MessageCode.DiscountPeriod.END_DATE_NOT_NULL)
    private String endDate;
    private Integer status;
    private String minPrice;
    private Long giftId;
//    @NotNull(message = MessageCode.DiscountPeriod.TYPE_PERIOD_NOT_NULL)
    private Integer typePeriod;
}