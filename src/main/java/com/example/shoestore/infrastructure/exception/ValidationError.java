package com.example.shoestore.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor

public class ValidationError<T> {
    private T object;

    private List<String> errors;

    public ValidationError() {
    }
}
