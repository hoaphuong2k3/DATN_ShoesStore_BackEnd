package com.example.shoestore.core.sale.cart.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.sale.cart.model.request.CheckoutDTO;
import com.example.shoestore.core.sale.cart.model.request.DeleteShoesCartDTO;
import com.example.shoestore.core.sale.cart.model.request.ShoesDetailDTO;
import com.example.shoestore.core.sale.cart.model.response.ShoesCartResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ShoesCartService {

    ResponseDTO addToCart(ShoesDetailDTO shoesDetailDTO);

    ResponseDTO removeValueInRedis(Long key, Long id);

    Page<ShoesCartResponse> findShoesInCart(int page,int size, Long key);

    ResponseDTO updateQuantity(ShoesDetailDTO shoesDetailDTO);

    ResponseDTO checkout( CheckoutDTO checkoutDTO);
    ResponseDTO findCheckout(Long key);
    ResponseDTO buyNow(ShoesDetailDTO shoesDetailDTO);
    ResponseDTO deleteShoesInCheckout(Long key , Long id);
    ResponseDTO deleteListShoesInCart(Long key , DeleteShoesCartDTO deleteShoesCartDTO);
}
