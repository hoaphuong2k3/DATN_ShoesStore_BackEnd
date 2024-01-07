package com.example.shoestore.infrastructure.constants;

public enum RoleAccount {

    ADMIN(1L),

    STAFF(2L),

    USER(3L);

    private final Long value;

    RoleAccount(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }
}
