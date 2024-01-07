package com.example.shoestore.core.product.admin.dto.request;

import com.example.shoestore.infrastructure.exception.ValidateException;
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

public class ShoesFileCreateRequest {

    private String data;

    private ShoesCreateRequest createRequest;

    private MultipartFile file;

    @SneakyThrows
    public ShoesCreateRequest getData(Validator validator) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        ShoesCreateRequest createRequest = null;
        try {
            createRequest = objectMapper.readValue(data, ShoesCreateRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BindingResult bindingResult = new BeanPropertyBindingResult(createRequest, "ShoesCreateRequest");
        validator.validate(createRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ValidateException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        return createRequest;
    }
}
