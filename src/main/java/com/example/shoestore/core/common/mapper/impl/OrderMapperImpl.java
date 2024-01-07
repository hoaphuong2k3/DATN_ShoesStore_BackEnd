package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.OrderMapper;
import com.example.shoestore.core.sale.cart.model.entity.ShoesCart;
import com.example.shoestore.core.sale.bill.model.request.CreateOrderDTO;
import com.example.shoestore.core.sale.bill.model.response.OrderResponse;
import com.example.shoestore.entity.*;
import com.example.shoestore.repository.MemberAccountRepository;
//import com.example.shoestore.repository.PaymentMethodRepository;
//import com.example.shoestore.repository.VouchersRepository;
import com.example.shoestore.repository.ClientRepository;
import com.example.shoestore.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderMapperImpl implements OrderMapper {

    @Autowired
    private MemberAccountRepository memberAccountRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private ClientRepository clientRepository;


    @Override
    public Order DTOToEntity(OrderResponse dto) {
        return null;
    }

    @Override
    public OrderResponse entityToDTO(Order entity) {
        if (entity == null) {
            return null;
        }
        OrderResponse dto = new OrderResponse();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        if (entity.getIdClient() != null) {
            Client client = clientRepository.findById(entity.getIdClient()).orElse(null);
            dto.setNameUser(client.getFullname());
        }

        dto.setPaymentMethods(entity.getPaymentMethod());
        if (entity.getIdDiscount() != null) {
            Discount discount = discountRepository.findById(entity.getIdDiscount()).orElse(null);
            dto.setCodeDiscount(discount.getName());
        }
        dto.setTotalMoney(entity.getTotalMoney());
        dto.setTotalPayment(entity.getTotalPayment());
        dto.setDatePayment(entity.getDatePayment());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setCreatedTime(entity.getCreatedTime());
        dto.setUpdatedTime(entity.getUpdatedTime());
        dto.setStatus(entity.getStatus());
        dto.setIsDeleted(entity.getIsDeleted());
        return dto;
    }

    @Override
    public Order createDTOToEntity(CreateOrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }
        Order entity = new Order();
        entity.setTotalMoney(orderDTO.getTotalMoney());
        return entity;
    }

    @Override
    public List<OrderDetail> listRedisToJPA(List<ShoesCart> shoesCartList) {
        List<OrderDetail> orderDetailList = shoesCartList.stream()
                .map(redis -> {
                    OrderDetail jpa = new OrderDetail();
                    jpa.setIdShoesDetails(redis.getId());
                    jpa.setQuantity(redis.getQuantity());
                    jpa.setTotalPrice(redis.getTotalPrice());
                    return jpa;
                }).collect(Collectors.toList());
        return orderDetailList;
    }

    @Override
    public OrderDetail redisToJPA(ShoesCart shoesCart) {
        if (shoesCart == null) {
            return null;
        }
        OrderDetail jpa = new OrderDetail();
        jpa.setIdShoesDetails(shoesCart.getId());
        jpa.setQuantity(shoesCart.getQuantity());
        jpa.setTotalPrice(shoesCart.getTotalPrice());
        return jpa;
    }
}
