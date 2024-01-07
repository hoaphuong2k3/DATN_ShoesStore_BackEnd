package com.example.shoestore.infrastructure.constants;

public enum ExchangeChoose {

    EXCHANGE(true),
    GIVE_BACK(false);

    private final boolean value;

    ExchangeChoose(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
