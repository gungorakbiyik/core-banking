package com.gungor.corebanking.domain.ledger;

import com.gungor.corebanking.common.Preconditions;
import com.gungor.corebanking.domain.account.AccountId;
import com.gungor.corebanking.domain.shared.Money;

public final class LedgerEntry {
    private final LedgerEntryId id;
    private final AccountId accountId;
    private final Money amount;
    private final Direction direction;

    private LedgerEntry(LedgerEntryId id,
                        AccountId accountId,
                        Money amount,
                        Direction direction) {
        Preconditions.notNull(id, "id");
        Preconditions.notNull(accountId, "accountId");
        Preconditions.notNull(amount, "amount");
        Preconditions.notNull(direction, "direction");

        if (!amount.isPositive()) {
            throw new IllegalArgumentException("Amount must be positive, but was: " + amount);
        }
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.direction = direction;
    }

    public LedgerEntryId getId() {
        return id;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public Money getAmount() {
        return amount;
    }

    public Direction getDirection() {
        return direction;
    }

    static LedgerEntry of(AccountId accountId,
                                 Money amount,
                                 Direction direction) {
        return new LedgerEntry(
                LedgerEntryId.newId(),
                accountId,
                amount,
                direction
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LedgerEntry entry)) return false;

        return id.equals(entry.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "LedgerEntry{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", amount=" + amount +
                ", direction=" + direction +
                '}';
    }
}
