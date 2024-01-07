package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.sale.cart.model.entity.ShoesCart;
import com.example.shoestore.core.sale.cart.model.request.ShoesDetailDTO;
import com.example.shoestore.core.sale.cart.model.response.ShoesCartResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ShoesCartMapper extends BaseMapper<ShoesCart, ShoesCartResponse> {

    ShoesCart addCartDTOToEntity(ShoesDetailDTO shoesDetailDTO);

    Page<ShoesCartResponse> pageEntityToDTO(Page<ShoesCart> shoesCartPage);

    Page<ShoesCart> listToPage(List<ShoesCart> list, int page, int size);

    List<ShoesCartResponse> listEntityToDTO(List<ShoesCart> shoesCartList);

}
