package com.example.shoestore.core.product.user.dto.response;

public interface UserShoesSearchByIdResponse {

    Long getId();
    String getCode();
    String getName();
    String getBrand();
    String getOrigin();
    String getDesignStyle();
    String getSkinType();
    String getSole();
    String getLining();
    String getToe();
    String getCushion();
    String getImgName();
    String getImgURI();
    Integer getPriceMin();
    Integer getPriceMax();
    Integer getDiscountPriceMin();
    Integer getDiscountPriceMax();

}
