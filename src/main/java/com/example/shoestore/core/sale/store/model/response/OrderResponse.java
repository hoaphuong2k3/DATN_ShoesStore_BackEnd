package com.example.shoestore.core.sale.store.model.response;

import com.example.shoestore.core.common.dto.BaseDTO;
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

    private BigDecimal shippingPrice;

    private LocalDateTime shippingDate;

    private LocalDateTime receivedDate;

    private Integer paymentMethods;

    private Integer status;

    private String nameUser;

    private String codeDiscount;


}
