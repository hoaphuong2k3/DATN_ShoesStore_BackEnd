package com.example.shoestore.core.sale.store.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDeliveryOrderDTO {
    private Long id;
    private LocalDateTime shipDate;
    private String deliveryAddress;
    private String recipientName;
    private String recipientPhone;
    private BigDecimal deliveryCost;
    private Integer status;
    private Boolean isDeleted;
    private Long idOrder;


}
