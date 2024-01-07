package com.example.shoestore.core.sale.delivery.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.sale.delivery.model.request.CreateDeliveryOrderDTO;
import com.example.shoestore.core.sale.delivery.model.request.UpdateDeliveryOrderDTO;
import com.example.shoestore.core.sale.delivery.model.response.DeliveryOrderResponse;
import org.springframework.data.domain.Page;

public interface DeliveryService {

    ResponseDTO createDelivery(CreateDeliveryOrderDTO deliveryOrderDTO);
    ResponseDTO updateDelivery(UpdateDeliveryOrderDTO deliveryOrderDTO);
    ResponseDTO deleteDelivery(Long id);
    ResponseDTO detailDelivery(Long id);
    Page<DeliveryOrderResponse> pageDelivery(int page, int size, String code,String date, Integer status);

    ResponseDTO updateStatus(Integer status, Long id);


}
