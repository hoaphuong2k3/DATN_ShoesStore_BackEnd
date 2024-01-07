package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.DeliveryOrderMapper;
import com.example.shoestore.core.sale.bill.model.request.UpdateDeliveryOrderDTO;
import com.example.shoestore.core.sale.delivery.model.response.DeliveryOrderResponse;
import com.example.shoestore.entity.DeliveryOrder;
import org.springframework.stereotype.Service;

@Service
public class DeliveryOrderMapperImpl implements DeliveryOrderMapper {

    @Override
    public DeliveryOrder updateDtoToEntity(UpdateDeliveryOrderDTO dto) {
        DeliveryOrder entity = new DeliveryOrder();
        entity.setId(dto.getId());
        entity.setDeliveryCost(dto.getDeliveryCost());
        entity.setRecipientName(dto.getRecipientName());
        entity.setRecipientPhone(dto.getRecipientPhone());
        entity.setAddress(dto.getDeliveryAddress());
        return entity;
    }

}
