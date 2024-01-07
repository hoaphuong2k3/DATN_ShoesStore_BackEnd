package com.example.shoestore.core.product.admin.dto.request;

import com.example.shoestore.core.product.admin.dto.response.ShoesDetailReportDocResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ShoesDetailReportRequest {

    private List<ShoesDetailReportDocResponse> shoesDetailReportDocResponses;
}
