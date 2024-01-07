package com.example.shoestore.infrastructure.constants;

public enum Status {

    ACTIVATE(1),
    INACTIVATE(0);

    private final int value;

    Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
