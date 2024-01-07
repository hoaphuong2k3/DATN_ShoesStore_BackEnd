package com.example.shoestore.core.product.admin.dto.response;

import java.time.LocalDateTime;

public interface ShoesFindOneResponse {

     String getCode();

     String getName();

     Long getBrandId();

     String getBrandName();

     Long getOriginId();

     String getOriginName();

     Long getDesignStyleId();

     String getDesignStyleName();

     Long getSkinTypeId();

     String getSkinTypeName();

     Long getSoleId();

     String getSoleName();

     Long getLiningId();

     String getLiningName();

     Long getToeId();

     String getToeName();

     Long getCushionId();

     String getCushionName();

     String getImgURI();

     String getImgName();

     String getCreatedBy();

     LocalDateTime getCreatedTime();

}
