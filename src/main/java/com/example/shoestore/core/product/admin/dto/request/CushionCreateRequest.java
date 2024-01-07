package com.example.shoestore.core.product.admin.dto.request;

import com.example.shoestore.infrastructure.constants.MessageCode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class CushionCreateRequest {

    @NotBlank(message = MessageCode.Cushion.NAME_NOT_NULL)
    @Length(max = 20,message = MessageCode.Cushion.NAME_MAX_LENGTH)
    private String name;
}
