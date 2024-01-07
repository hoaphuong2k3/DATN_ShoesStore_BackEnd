package com.example.shoestore.core.product.user.dto.response;

import com.example.shoestore.infrastructure.constants.DiscountTypeProductResponse;
import com.example.shoestore.infrastructure.utils.DataUtils;

import java.math.BigDecimal;

public interface UserShoesDetailResponse {

    Long getId();
    String getName();
    String getBrand();
    String getOrigin();
    String getDesignStyle();
    String getSkinType();
    String getSole();
    String getLining();
    String getToe();
    String getCushion();
    String getColor();
    String getSize();
    BigDecimal getPrice();
    BigDecimal getDiscountPrice();
    Integer getQuantity();
    String getDescription();
    BigDecimal salePercent();

    default Integer getDiscountType(){
        if(getPrice().equals(getDiscountPrice())){
            return DiscountTypeProductResponse.NOT_DISCOUNT.getValue();
        }
        else if(DataUtils.isNotNull(salePercent())){
            return DiscountTypeProductResponse.PERCENT.getValue();
        }
        else{
            return DiscountTypeProductResponse.MONEY.getValue();
        }
    }

}
