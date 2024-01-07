package com.example.shoestore.infrastructure.constants;

public enum AvailableStatus {
    YES(0),
    NO(1);
    private final int value;

    AvailableStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
