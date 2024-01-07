//package com.example.shoestore.core.discount.discountperiod.dto.request;
//
//import com.example.shoestore.core.common.dto.BaseDTO;
//import com.example.shoestore.infrastructure.constants.MessageCode;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import lombok.Value;
//import org.hibernate.validator.constraints.Length;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//
///**
// * DTO for {@link com.example.shoestore.entity.DiscountPeriod}
// */
//@Value
//public class DiscountPeriodRequest extends BaseDTO implements Serializable {
//
//    @NotNull(message = MessageCode.DiscountPeriod.NAME_NOT_NULL)
//    @NotBlank(message = MessageCode.DiscountPeriod.NAME_NOT_BLANK)
//    @Length(max = 255, message = MessageCode.DiscountPeriod.NAME_MAX_LENGTH)
//    private String name;
//
//    private String code;
//
//    @NotNull(message = MessageCode.DiscountPeriod.SALE_PERCENT_NOT_NULL)
//    private String salePercent;
//
//    @NotNull(message = MessageCode.DiscountPeriod.START_DATE_NOT_NULL)
//    private String startDate;
//    @NotNull(message = MessageCode.DiscountPeriod.END_DATE_NOT_NULL)
//    private String endDate;
//    private Integer status;
//}