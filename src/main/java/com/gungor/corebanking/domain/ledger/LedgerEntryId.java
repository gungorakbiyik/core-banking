package com.gungor.corebanking.domain.ledger;

import com.gungor.corebanking.common.Preconditions;

import java.util.UUID;

public record LedgerEntryId(UUID value) {

    public LedgerEntryId {
        Preconditions.notNull(value, "value");
    }

    public static LedgerEntryId newId() {
        return new LedgerEntryId(UUID.randomUUID());
    }

    public static LedgerEntryId of(UUID value) {
        return new LedgerEntryId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
