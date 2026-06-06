package com.gungor.corebanking.domain.model;

import com.gungor.corebanking.common.Preconditions;

import java.util.UUID;

public record AccountId(UUID value) {

    public AccountId {
        Preconditions.notNull(value, "value");
    }

    public static AccountId newId() {
        return new AccountId(UUID.randomUUID());
    }

    public static AccountId of(UUID value) {
        return new AccountId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
