package com.example.shoestore.core.product.admin.dto.request;

import com.example.shoestore.infrastructure.constants.MessageCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ShoesUpdateRequest {

    @NotNull(message = MessageCode.Shoes.NAME_NOT_NULL)
    @NotBlank(message = MessageCode.Shoes.NAME_NOT_BLANK)
    @Length(max = 50, message = MessageCode.Shoes.NAME_MAX_LENGTH)
    private String name;

    @NotNull(message = MessageCode.Shoes.BRAND_ID_NOT_NULL)
    private Long brandId;

    @NotNull(message = MessageCode.Shoes.ORIGIN_ID_NOT_NULL)
    private Long originId;

    @NotNull(message = MessageCode.Shoes.TYPE_ID_NOT_NULL)
    private Long designStyleId;

    @NotNull(message = MessageCode.Shoes.SKIN_TYPE_ID_NOT_NULL)
    private Long skinTypeId;

    @NotNull(message = MessageCode.Shoes.SOLE_ID_NOT_NULL)
    private Long soleId;

    @NotNull(message = MessageCode.Shoes.LINING_ID_NOT_NULL)
    private Long liningId;

    @NotNull(message = MessageCode.Shoes.TOE_ID_NOT_NULL)
    private Long toeId;

    @NotNull(message = MessageCode.Shoes.CUSHION_ID_NOT_NULL)
    private Long cushionId;

    @Length(max = 255,message = MessageCode.Shoes.DESCRIPTION_MAX_LENGTH)
    private String description;

}
