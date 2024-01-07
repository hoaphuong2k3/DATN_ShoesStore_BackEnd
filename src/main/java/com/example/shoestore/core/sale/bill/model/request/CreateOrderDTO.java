package com.example.shoestore.core.sale.bill.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDTO {

    private Long idClient;
    private Long idStaff;
    private Long idDisCountPeriod;
    private Long idVoucher;
    private List<Long> idShoesDetail;
    private Integer paymentMethod;
    private Integer points;
    private Integer totalPoints;
    private BigDecimal totalMoney;
    private BigDecimal totalPayment;
    private Boolean usingPoints;
    private CreateDeliveryOrderDTO deliveryOrderDTO;


}
