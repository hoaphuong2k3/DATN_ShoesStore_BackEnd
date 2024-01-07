package com.example.shoestore.core.sale.store.service;


import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.sale.store.model.request.CreateOrderStoreDTO;

public interface AdminOrderStoreService {
    ResponseDTO createOrderStore(CreateOrderStoreDTO orderStoreDTO);
    ResponseDTO findDiscountPeriod();
}
