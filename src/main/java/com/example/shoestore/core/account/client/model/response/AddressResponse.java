package com.example.shoestore.core.account.client.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private Long id;
    private String proviceCode;
    private String districtCode;
    private String communeCode;
    private String addressDetail;
}
