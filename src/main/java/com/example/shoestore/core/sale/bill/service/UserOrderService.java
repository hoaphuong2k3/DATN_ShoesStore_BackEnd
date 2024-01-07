package com.example.shoestore.core.sale.bill.service;

import com.example.shoestore.core.common.dto.response.FileResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.sale.bill.model.request.BillPrintRequestDTO;
import com.example.shoestore.core.sale.bill.model.request.CreateOrderDTO;
import com.example.shoestore.core.sale.bill.model.request.UpdateStatusDTO;
import com.example.shoestore.core.sale.bill.model.response.UserOrderDetailResponse;
import org.springframework.data.domain.Page;


public interface UserOrderService {

    ResponseDTO createBill(CreateOrderDTO orderDTO);

    ResponseDTO updateBill(UpdateStatusDTO statusDTO);

    Page<UserOrderDetailResponse> findOrder(Long idClient, Integer status, int page, int size);

    ResponseDTO detailOrder(Long idOrder);

    FileResponseDTO printBill(Long idOrder, BillPrintRequestDTO requestDTO);
}
