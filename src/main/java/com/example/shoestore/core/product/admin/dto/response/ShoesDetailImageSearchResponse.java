package com.example.shoestore.core.product.admin.dto.response;

import com.example.shoestore.core.product.admin.dto.ImageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ShoesDetailImageSearchResponse {

    private ShoesDetailSearchResponse shoesDetailSearchResponse;

    private List<ImageDTO> imageDTOS;
}
