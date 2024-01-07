package com.example.shoestore.core.account.staff.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAddressDTO {
    private String proviceCode;

    private String districtCode;

    private String communeCode;

    private String addressDetail;

    private Boolean isDeleted;
}
