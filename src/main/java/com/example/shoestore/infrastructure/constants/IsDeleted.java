package com.example.shoestore.infrastructure.constants;

public enum IsDeleted {

    DELETED(true),
    UNDELETED(false);

    private final boolean value;

    IsDeleted(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
