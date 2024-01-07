package com.example.shoestore.core.sale.bill.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusDTO {

    private Long idClient;
    private Long idOrder;

    private Integer status;

}
