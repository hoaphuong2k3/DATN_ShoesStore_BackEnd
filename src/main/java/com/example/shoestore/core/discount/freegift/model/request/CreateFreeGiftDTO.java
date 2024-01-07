package com.example.shoestore.core.discount.freegift.model.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFreeGiftDTO {


    @NotBlank(message = "Tên không được để trống!")
    @Size(max = 1000, message = "Tên phải nhỏ hơn 1000 kí tự")
    private String name;

    @NotNull(message = "Quantity không được bỏ trống!")
    @Min(value = 1, message = "số lượng phải lớn hơn hoặc bằng 1")
    @Max(value = 100000, message = "số lượng phải nhỏ hơn hoặc bằng 100000")
    private Integer quantity;

}
