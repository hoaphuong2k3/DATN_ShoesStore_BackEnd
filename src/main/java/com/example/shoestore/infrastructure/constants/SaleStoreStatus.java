package com.example.shoestore.infrastructure.constants;

public enum SaleStoreStatus {
    SALE_STORE(true),
    SALE_ONLINE(false);

    private final boolean value;

    SaleStoreStatus(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
