package com.example.shoestore.core.account.client.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAddressDTO {
    private Long id;
    private String proviceCode;

    private String districtCode;

    private String communeCode;

    @NotBlank(message = "Không bỏ trống")
    @Size(max = 1000, message = "Không vượt quá 1000 kí tự")
    private String addressDetail;
    private Long idClient;

    private Boolean isDeleted;
}
