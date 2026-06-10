package com.gungor.corebanking.domain.customer;

import com.gungor.corebanking.common.Preconditions;

import java.util.UUID;

public record CustomerId(UUID value) {

    public CustomerId {
        Preconditions.notNull(value, "value");
    }

    public static CustomerId newId() {
        return new CustomerId(UUID.randomUUID());
    }

    public static CustomerId of(UUID value) {
        return new CustomerId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
