package com.example.shoestore.core.sale.bill.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExportOrderResponse {
    private Integer stt;
    private String code;
    private String nameAdmin;
    private String nameClient;
    private BigDecimal totalMoney;
    private BigDecimal totalPayment;
    private String paymentMethod;
    private LocalDateTime datePayment;
    private LocalDateTime createdTime;
    private String saleStatus;
    private Integer points;
    private String status;


}
