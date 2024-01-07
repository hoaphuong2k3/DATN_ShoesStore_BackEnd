package com.example.shoestore.infrastructure.constants;

public enum ExchangeStatus {
    CANCEL(-1),
    WAIT_CONFIRM(0),
    PROCESSING(1),
    PROCESSED(2);

    private final int value;

    ExchangeStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
