package com.example.shoestore.infrastructure.constants;

public enum DiscountStatus {

    RUNNING(0),

    WAITING(1),

    STOP(2);

    private final int value;

    DiscountStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
