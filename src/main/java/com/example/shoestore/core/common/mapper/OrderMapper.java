package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.sale.cart.model.entity.ShoesCart;
import com.example.shoestore.core.sale.bill.model.request.CreateOrderDTO;
import com.example.shoestore.core.sale.bill.model.response.OrderResponse;
import com.example.shoestore.entity.Order;
import com.example.shoestore.entity.OrderDetail;

import java.util.List;

public interface OrderMapper extends BaseMapper<Order , OrderResponse> {

    Order createDTOToEntity(CreateOrderDTO orderDTO);

    List<OrderDetail> listRedisToJPA(List<ShoesCart> shoesCartList);
    OrderDetail redisToJPA(ShoesCart shoesCart);
}
