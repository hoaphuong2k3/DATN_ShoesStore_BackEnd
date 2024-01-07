package com.example.shoestore.core.sale.bill.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailBillResponse {
    private Long id;
    private String code;
    private BigDecimal totalMoney;
    private BigDecimal totalPayment;
    private BigDecimal shipPrice;
    private LocalDateTime createTime;
    private LocalDateTime cancellationDate;
    private LocalDateTime receivedDate;
    private LocalDateTime shipDate;
    private String nameDelivery;
    private String addressDelivery;
    private String phonenumberDelivery;
    private Integer status;
    private List<CartResponse> listCart;
}
