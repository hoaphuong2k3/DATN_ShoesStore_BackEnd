package com.example.shoestore.core.sale.bill.service;

import com.example.shoestore.core.common.dto.response.FileResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.sale.bill.model.request.CreateShoesInCart;
import com.example.shoestore.core.sale.bill.model.request.UpdateDeliveryOrderDTO;
import com.example.shoestore.core.sale.bill.model.request.UpdateShoesInCart;
import com.example.shoestore.core.sale.bill.model.response.CartResponse;
import com.example.shoestore.core.sale.bill.model.response.OrderStatusResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;


public interface AdminOrderService {

    Page<OrderStatusResponse> findAll(Integer status,String code, int page, int size);

    FileResponseDTO exportExcel(String date, Integer status);

    ResponseDTO updateStatus(Integer status,Long idStaff, Long idOrder);

    List<CartResponse> listCart(Long idOrder);

    ResponseDTO updateDeliveryOrder(UpdateDeliveryOrderDTO deliveryOrderDTO);

    ResponseDTO changeStatusDeliveryOrder(Long id, Integer status);

    ResponseDTO deliveryDetail(Long idOrder);

    ResponseDTO updateShoesInCart(UpdateShoesInCart shoesDTO);

    ResponseDTO deleteShoesInCart(Long idShoesInCart);

    ResponseDTO createShoesInCart(CreateShoesInCart shoesDTO);
    ResponseDTO countStatus(Integer status);


    ResponseDTO updateTotalmoney(BigDecimal totalMoney ,BigDecimal totalPayment, Long idOrder);
}
