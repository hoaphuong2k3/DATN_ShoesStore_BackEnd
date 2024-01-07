package com.example.shoestore.core.product.admin.dto;

import com.example.shoestore.core.common.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ShoesDTO extends BaseDTO {

    private String code;

    private String name;

    private Long brandId;

    private Long originId;

    private Long designStyleId;

    private Long skinTypeId;

    private Long soleId;

    private Long liningId;

    private Long toeId;

    private Long cushionId;

    private String description;

    private String imgURI;

    private String imgName;

}
