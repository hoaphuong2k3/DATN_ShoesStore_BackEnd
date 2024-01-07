package com.example.shoestore.core.product.admin.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class ShoesDetailImageDTO {

    private List<ShoesDetailDTO> shoesDetailDTOS;

    private List<ImageDTO> imageDTOS;
}
