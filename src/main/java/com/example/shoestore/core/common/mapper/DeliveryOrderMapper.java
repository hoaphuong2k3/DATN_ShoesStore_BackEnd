package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.sale.bill.model.request.CreateDeliveryOrderDTO;
import com.example.shoestore.core.sale.bill.model.request.UpdateDeliveryOrderDTO;
import com.example.shoestore.core.sale.delivery.model.response.DeliveryOrderResponse;
import com.example.shoestore.entity.DeliveryOrder;

public interface DeliveryOrderMapper  {

    DeliveryOrder updateDtoToEntity(UpdateDeliveryOrderDTO dto);


}
