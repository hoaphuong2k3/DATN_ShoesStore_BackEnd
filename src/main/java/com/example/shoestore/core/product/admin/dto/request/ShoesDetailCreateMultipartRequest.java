package com.example.shoestore.core.product.admin.dto.request;

import com.example.shoestore.infrastructure.exception.ValidateException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ShoesDetailCreateMultipartRequest {

    private String data;

    private List<MultipartFile> files;

    private List<ShoesDetailCreateRequest> shoesDetailCreateRequest;

    @SneakyThrows
    public List<ShoesDetailCreateRequest> getData(Validator validator) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        List<ShoesDetailCreateRequest> createRequests = objectMapper.readValue(data, objectMapper.getTypeFactory().constructCollectionType(List.class, ShoesDetailCreateRequest.class));

        BindingResult bindingResult = new BeanPropertyBindingResult(createRequests, "shoesDetailCreateRequest");
        validator.validate(createRequests, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ValidateException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        return createRequests;
    }

}
