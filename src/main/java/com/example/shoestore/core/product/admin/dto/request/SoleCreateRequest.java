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

public class SoleCreateRequest {

    @NotBlank(message = MessageCode.Sole.NAME_NOT_NULL)
    @Length(max = 20,message = MessageCode.Sole.NAME_MAX_LENGTH)
    private String name;

}
