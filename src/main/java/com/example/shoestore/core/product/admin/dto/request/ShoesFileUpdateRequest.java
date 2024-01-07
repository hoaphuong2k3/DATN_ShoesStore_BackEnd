package com.example.shoestore.core.product.admin.dto.request;

import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.DataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ShoesFileUpdateRequest {

    private String data;

    private ShoesUpdateRequest updateRequest;

    private MultipartFile file;

    private String isChange;

    public Boolean getIsChange() {
        if (DataUtils.isNull(isChange) || DataUtils.isBlank(isChange) || isChange.equalsIgnoreCase(Boolean.FALSE.toString())) {
            return null;
        } else {
            return Boolean.TRUE;
        }
    }

    @SneakyThrows
    public ShoesUpdateRequest getData(Validator validator) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        ShoesUpdateRequest updateRequest = null;
        try {
            updateRequest = objectMapper.readValue(data, ShoesUpdateRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BindingResult bindingResult = new BeanPropertyBindingResult(updateRequest, "ShoesCreateRequest");
        validator.validate(updateRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ValidateException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        return updateRequest;
    }
}
