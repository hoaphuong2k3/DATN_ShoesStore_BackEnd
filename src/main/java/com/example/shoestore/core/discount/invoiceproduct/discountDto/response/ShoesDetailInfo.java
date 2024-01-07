package com.example.shoestore.core.discount.invoiceproduct.discountDto.response;

/**
 * Projection for {@link com.example.shoestore.entity.ShoesDetail}
 */
public interface ShoesDetailInfo {

    Long getIdShoeDetail();
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
    Integer getDiscountPrice();
    Integer getOriginPrice();
    String getSize();
    String getColor();
}