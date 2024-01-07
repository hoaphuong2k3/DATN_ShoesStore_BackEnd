package com.example.shoestore.infrastructure.constants;

public enum DiscountTypeProductResponse {

    NOT_DISCOUNT(0),

    PERCENT(1),

    MONEY(2);

    private final int value;

    DiscountTypeProductResponse(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
