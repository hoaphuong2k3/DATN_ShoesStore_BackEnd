package com.example.shoestore.core.discount.freegift.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFreeGiftDTO {

    private Long id;

    private String code;
    private String name;

    private Integer quantity;

    private Integer status;

}
