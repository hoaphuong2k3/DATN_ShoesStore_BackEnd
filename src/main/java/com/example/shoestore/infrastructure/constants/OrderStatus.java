package com.example.shoestore.infrastructure.constants;

public enum OrderStatus {

    CANCELED(7),
    WAITING_CONFIRM(0),
    AWAITING_SHIPPING(1),
    SHIPPING(2),
    SUCCESSFULL_SHIPPING(3),
    WAITING_EXCHANGE(4),
    EXCHANGED(5),
    RECEIVED(6);

    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
