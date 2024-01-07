package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.DiscountShoesDetailMapper;
import com.example.shoestore.core.discount.invoiceproduct.discountShoesDto.response.DiscountsShoesDetailResponse;
import com.example.shoestore.entity.DiscountsShoesDetail;

public class DiscountShoesDetailMapperIpml implements DiscountShoesDetailMapper {

    @Override
    public DiscountsShoesDetail DTOToEntity(DiscountsShoesDetailResponse dto) {
        DiscountsShoesDetail discountsShoesDetail = new DiscountsShoesDetail();
        discountsShoesDetail.setPromoId(dto.getPromoId());
        discountsShoesDetail.setShoesDetailId(dto.getShoesDetailId());
        discountsShoesDetail.setDiscountPrice(dto.getDiscountPrice());
        discountsShoesDetail.setId(dto.getId());
        return discountsShoesDetail;
    }

    @Override
    public DiscountsShoesDetailResponse entityToDTO(DiscountsShoesDetail entity) {
        DiscountsShoesDetailResponse discountsShoesDetailResponse = new DiscountsShoesDetailResponse();
        discountsShoesDetailResponse.setId(entity.getId());
        discountsShoesDetailResponse.setPromoId(entity.getPromoId());
        discountsShoesDetailResponse.setShoesDetailId(entity.getShoesDetailId());
        discountsShoesDetailResponse.setDiscountPrice(entity.getDiscountPrice());
        return discountsShoesDetailResponse;
    }
}
