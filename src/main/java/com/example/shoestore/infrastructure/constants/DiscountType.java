package com.example.shoestore.infrastructure.constants;

public enum DiscountType {

    VOUCHERS(0),

    PROMOS(1);

    private final int value;

    DiscountType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
