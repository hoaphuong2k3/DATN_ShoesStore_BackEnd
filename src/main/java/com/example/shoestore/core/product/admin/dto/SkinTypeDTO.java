package com.example.shoestore.core.product.admin.dto;

import com.example.shoestore.core.common.dto.BaseDTO;
import com.example.shoestore.infrastructure.constants.MessageCode;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SkinTypeDTO extends BaseDTO {

    private String code;

    @NotNull(message = MessageCode.DesignStyle.NAME_NOT_NULL)
    private String name;

}
