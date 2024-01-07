package com.example.shoestore.core.sale.bill.model.response;


import com.example.shoestore.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOrderDetailResponse {
    private Long id;
    private String code;
    private BigDecimal totalPayment;
    private LocalDateTime createTime;
    private LocalDateTime cancellationDate;
    private LocalDateTime receivedDate;
    private LocalDateTime shipDate;
    private Integer status;
    private List<CartResponse> listCart;
}
