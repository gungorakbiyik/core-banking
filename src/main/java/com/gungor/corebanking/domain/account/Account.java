package com.gungor.corebanking.domain.account;

import com.gungor.corebanking.common.Preconditions;
import com.gungor.corebanking.domain.customer.CustomerId;

import java.time.Instant;
import java.util.Currency;

public final class Account {
    private final AccountId id;
    private final CustomerId customerId;
    private final AccountType type;
    private final Currency currency;
    private AccountStatus status;
    private final Instant openedAt;

    private Account(AccountId id,
                    CustomerId customerId,
                    AccountType type,
                    Currency currency,
                    AccountStatus status,
                    Instant openedAt) {

        Preconditions.notNull(id, "id");
        Preconditions.notNull(customerId, "customerId");
        Preconditions.notNull(type, "type");
        Preconditions.notNull(currency, "currency");
        Preconditions.notNull(status, "status");
        Preconditions.notNull(openedAt, "openedAt");

        this.id = id;
        this.customerId = customerId;
        this.type = type;
        this.currency = currency;
        this.status = status;
        this.openedAt = openedAt;
    }

    public AccountId getId() {
        return id;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public AccountType getType() {
        return type;
    }

    public Currency getCurrency() {
        return currency;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public Instant getOpenedAt() {
        return openedAt;
    }

    public static Account open(CustomerId customerId, AccountType type, Currency currency) {
        return new Account(
                AccountId.newId(),
                customerId,
                type,
                currency,
                AccountStatus.ACTIVE,
                Instant.now()
        );
    }

    public void freeze() {
        if (status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Only an active account can be frozen, but was :" + status);
        }
        this.status = AccountStatus.FROZEN;
    }

    public void close() {
        if (status != AccountStatus.ACTIVE && status != AccountStatus.FROZEN) {
            throw new IllegalStateException("Only an active or frozen account can be closed, but was :" + status);
        }
        this.status = AccountStatus.CLOSED;
    }

    public void unfreeze() {
        if (status != AccountStatus.FROZEN) {
            throw new IllegalStateException("Only a frozen account can be unfrozen, but was :" + status);
        }
        this.status = AccountStatus.ACTIVE;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Account account)) return false;

        return id.equals(account.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
