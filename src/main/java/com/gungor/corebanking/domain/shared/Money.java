package com.gungor.corebanking.domain.shared;

import com.gungor.corebanking.common.Preconditions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public final class Money {
    private final BigDecimal amount;
    private final Currency currency;

    private Money(BigDecimal amount, Currency currency) {
        Preconditions.notNull(amount, "amount");
        Preconditions.notNull(currency, "currency");

        this.amount = amount.setScale(
                currency.getDefaultFractionDigits(),
                RoundingMode.HALF_EVEN
        );
        this.currency = currency;
    }

    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    public Money plus(Money other) {
        Preconditions.notNull(other, "other");
        requireSameCurrency(other);
        return Money.of(amount.add(other.amount), currency);
    }

    public Money minus(Money other) {
        Preconditions.notNull(other, "other");
        requireSameCurrency(other);
        return Money.of(amount.subtract(other.amount), currency);
    }

    public Money multiply(BigDecimal factor) {
        Preconditions.notNull(factor, "factor");
        return Money.of(amount.multiply(factor), currency);
    }

    public boolean isNegative() {
        return amount.signum() < 0;
    }

    public boolean isZero() {
        return amount.signum() == 0;
    }

    public boolean isPositive() {
        return amount.signum() > 0;
    }

    private void requireSameCurrency(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("currency mismatch: " + currency + " vs " + other.currency);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return amount.equals(money.amount) && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency.getCurrencyCode();
    }
}
