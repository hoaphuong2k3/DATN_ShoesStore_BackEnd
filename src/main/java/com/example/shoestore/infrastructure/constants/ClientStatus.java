package com.example.shoestore.infrastructure.constants;

public enum ClientStatus {


    ACTIVE(0),
    INACTIVE(1);
    private final int value;

    ClientStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
