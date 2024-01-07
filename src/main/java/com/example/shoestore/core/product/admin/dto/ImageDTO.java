package com.example.shoestore.core.product.admin.dto;

import com.example.shoestore.core.common.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ImageDTO extends BaseDTO {

    private Long shoesDetailId;

    private String imgName;

    private String imgURI;

}
