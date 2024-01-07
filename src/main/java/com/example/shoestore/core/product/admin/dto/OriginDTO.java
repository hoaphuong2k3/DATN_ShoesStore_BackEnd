package com.example.shoestore.core.product.admin.dto;

import com.example.shoestore.core.common.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class OriginDTO extends BaseDTO {

    private String code;

    private String name;

}
