package com.example.shoestore.infrastructure.constants;

public enum DeliveryOrderStatus {

    CANCEL(-1),
    WAITING_SHIP(0),
    SHIPPING(1),
    SHIP_SUCCESS(2);

    private final int value;

    DeliveryOrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
