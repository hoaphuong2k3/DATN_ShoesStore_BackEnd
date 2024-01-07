package com.example.shoestore.core.sale.bill.model.response;

import com.example.shoestore.core.common.dto.BaseDTO;
import com.example.shoestore.core.sale.store.model.response.DiscountPeriodsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse extends BaseDTO {

    private Long id;
    private String code;

    private BigDecimal totalMoney;

    private BigDecimal totalPayment;

    private LocalDateTime datePayment;

    private Integer paymentMethods;

    private Integer status;

    private String nameUser;

    private String codeDiscount;

    private DiscountPeriodsResponse discountPeriodsResponse;

}
