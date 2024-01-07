package com.example.shoestore.core.sale.store.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDTO {

    private Long idAccount;
    private Long idDisCount;
    private List<Long> idShoesDetail;
    private Integer paymentMethod;
    private BigDecimal totalMoney;
    private CreateDeliveryDTO deliveryOrderDTO;


}
