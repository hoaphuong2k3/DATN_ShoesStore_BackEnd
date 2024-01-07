package com.example.shoestore.infrastructure.constants;

public enum ShoesStatus {

    OUT_OF_STOCK(-1),
    STOP_BUSINESS(0),
    ON_BUSINESS(1);

    private final int value;

    ShoesStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
