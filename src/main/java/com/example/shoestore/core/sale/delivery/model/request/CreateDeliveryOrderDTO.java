package com.example.shoestore.core.sale.delivery.model.request;

import com.example.shoestore.infrastructure.constants.MessageCode;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeliveryOrderDTO {
    @NotBlank(message = MessageCode.DeliveryOrder.DELIVERY_ADDRESS)
    @Size(max = 1000, message = MessageCode.DeliveryOrder.DELIVERY_ADDRESS_MAX)
    private String deliveryAddress;
    @NotBlank(message = MessageCode.DeliveryOrder.DELIVERY_RECIPIENT_NAME)
    @Size(max = 255, message = MessageCode.DeliveryOrder.DELIVERY_RECIPIENT_NAME_MAX)
    private String recipientName;

    @NotBlank(message = MessageCode.DeliveryOrder.DELIVERY_RECIPIENT_PHONE)
    @Size(max = 15, message = MessageCode.DeliveryOrder.DELIVERY_RECIPIENT_PHONE_MAX)
    private String recipientPhone;

    @NotNull(message = MessageCode.DeliveryOrder.DELIVERY_DELIVERY_COST)
    @DecimalMin(value = "1000.0", inclusive = true, message = MessageCode.DeliveryOrder.DELIVERY_DELIVERY_COST_MAX)
    @DecimalMax(value = "1000000000.0", inclusive = true, message = MessageCode.DeliveryOrder.DELIVERY_DELIVERY_COST_MIN)
    private BigDecimal deliveryCost;

//    @NotNull(message = MessageCode.DeliveryOrder.DELIVERY_ORDER_NULL)
    private Long idOrder;



}
