package com.example.shoestore.infrastructure.constants;

public enum DiscountPeriodType {

    ORDER(0),

    FREE_SHIP(1);

    private final int value;

    DiscountPeriodType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
