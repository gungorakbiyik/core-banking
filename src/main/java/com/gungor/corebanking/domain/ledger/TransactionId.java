package com.gungor.corebanking.domain.ledger;

import com.gungor.corebanking.common.Preconditions;

import java.util.UUID;

public record TransactionId(UUID value) {

    public TransactionId {
        Preconditions.notNull(value, "value");
    }

    public static TransactionId newId() {
        return new TransactionId(UUID.randomUUID());
    }

    public static TransactionId of(UUID value) {
        return new TransactionId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
