package com.example.shoestore.core.product.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LiningSearchRequest {

    private String codeOrName;
}
