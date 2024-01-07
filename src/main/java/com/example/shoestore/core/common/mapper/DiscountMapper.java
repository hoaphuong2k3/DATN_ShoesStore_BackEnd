package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.request.DiscountRequest;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.request.PromoRequest;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.request.UpdatePromoRequest;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.response.DiscountResponse;
import com.example.shoestore.entity.Discount;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DiscountMapper extends BaseMapper<Discount, DiscountResponse>{

    Discount createDTOToEntity(DiscountRequest createDiscountDTO);

    Discount updateDTOToEntity(DiscountRequest createDiscountDTO);

    Discount createPromoDTOToEntity(PromoRequest promoDTO);

    Discount updatePromoDTOToEntity(PromoRequest promoDTO);

    List<DiscountResponse> entityListToDTOList(List<Discount> entityList);

    Page<DiscountResponse> pageEntityTODTO(Page<Discount> pageDiscount);

}
