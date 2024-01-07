package com.example.shoestore.core.sale.store.model.request;

import com.example.shoestore.core.sale.store.model.response.DiscountPeriodsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderStoreDTO {
    private BigDecimal totalMoney;
    private BigDecimal totalPayment;
    private Integer paymentMethod;
    private Long idDiscountPeriods;
    private Long idClient;
    private Long idStaff;
    private Integer totalPoints;
    private Boolean usingPoints;
    private List<CreateShoesInCart> shoesInCart;
    private CreateDeliveryDTO deliveryOrderDTO;
}
