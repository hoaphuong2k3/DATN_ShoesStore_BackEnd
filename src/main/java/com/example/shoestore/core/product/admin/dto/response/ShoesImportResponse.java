package com.example.shoestore.core.product.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ShoesImportResponse {

    private Integer stt;

    private String name;

    private Long designStyleId;

    private Long brandId;

    private Long liningId;

    private Long soleId;

    private Long skinTypeId;

    private Long originId;

    private Long cushionId;

    private Long toeId;

    private String imgURI;

    private String imgName;

    private String description;

    private String error;

}
