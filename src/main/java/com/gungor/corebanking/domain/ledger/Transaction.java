package com.gungor.corebanking.domain.ledger;

import com.gungor.corebanking.common.Preconditions;
import com.gungor.corebanking.domain.account.AccountId;
import com.gungor.corebanking.domain.shared.Money;

import java.time.Instant;
import java.util.Currency;
import java.util.List;

public final class Transaction {
    private final TransactionId id;
    private final List<LedgerEntry> entries;
    private final Instant occurredAt;

    private Transaction(TransactionId id,
                        List<LedgerEntry> entries,
                        Instant occurredAt) {

        Preconditions.notNull(id, "id");
        Preconditions.notNull(entries, "entries");
        Preconditions.notNull(occurredAt, "occurredAt");

        for (LedgerEntry entry : entries) {
            Preconditions.notNull(entry, "entry");
        }

        if (entries.size() < 2) {
            throw new IllegalArgumentException("a transaction must have at least 2 entries, but had: " + entries.size());
        }


        // currency check; all entries must share one currency
        Currency currency = entries.getFirst().getAmount().currency();
        for (LedgerEntry entry : entries) {
            if (!entry.getAmount().currency().equals(currency)) {
                throw new IllegalArgumentException("all entries must share one currency, but found " +
                        entry.getAmount().currency() + " and " + currency);
            }
        }

        Money debitTotal = sumByDirection(entries, Direction.DEBIT);
        Money creditTotal = sumByDirection(entries, Direction.CREDIT);
        if (!debitTotal.equals(creditTotal)) {
            throw new IllegalArgumentException("debits must equal credits, but debit=" + debitTotal + " and credit=" + creditTotal);
        }

        this.id = id;
        this.entries = List.copyOf(entries);
        this.occurredAt = occurredAt;
    }

    public TransactionId getId() {
        return id;
    }

    public List<LedgerEntry> getEntries() {
        return entries;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    private static Money sumByDirection(List<LedgerEntry> entries, Direction direction) {
        return entries.stream()
                .filter(entry -> entry.getDirection() == direction)
                .map(LedgerEntry::getAmount)
                .reduce(Money::plus)
                .orElseThrow(() -> new IllegalArgumentException("transaction must have at least one " + direction + " entry"));
    }

    static Transaction of(List<LedgerEntry> entries) {
        return new Transaction(TransactionId.newId(), entries, Instant.now());
    }

    public static Transaction transfer(AccountId from, AccountId to, Money amount) {
        LedgerEntry debit = LedgerEntry.of(from, amount, Direction.DEBIT);
        LedgerEntry credit = LedgerEntry.of(to, amount, Direction.CREDIT);

        return of(List.of(debit, credit));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Transaction transaction)) return false;
        return id.equals(transaction.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
