package com.gungor.corebanking.domain.ledger;

import com.gungor.corebanking.domain.account.AccountId;
import com.gungor.corebanking.domain.shared.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

class LedgerEntryTest {

    private static final Currency TRY = Currency.getInstance("TRY");

    @Test
    void of_setsFields() {
        AccountId accountId = AccountId.newId();
        Money amount = money("100.00");

        LedgerEntry entry = LedgerEntry.of(accountId, amount, Direction.DEBIT);

        assertEquals(accountId, entry.getAccountId());
        assertEquals(amount, entry.getAmount());
        assertEquals(Direction.DEBIT, entry.getDirection());
    }

    @Test
    void of_generatesNonNullId() {
        LedgerEntry entry = LedgerEntry.of(
                AccountId.newId(),
                money("100.00"),
                Direction.DEBIT
        );
        assertNotNull(entry.getId());
    }

    @Test
    void of_generatesUniqueIds() {
        LedgerEntry entry1 = LedgerEntry.of(AccountId.newId(), money("100.00"), Direction.DEBIT);
        LedgerEntry entry2 = LedgerEntry.of(AccountId.newId(), money("100.00"), Direction.DEBIT);

        assertNotEquals(entry1.getId(), entry2.getId());
    }

    @Test
    void of_throws_whenAccountIdNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> LedgerEntry.of(null, money("100.00"), Direction.DEBIT));
    }

    @Test
    void of_throws_whenAmountNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> LedgerEntry.of(AccountId.newId(), null, Direction.DEBIT));
    }

    @Test
    void of_throws_whenDirectionNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> LedgerEntry.of(AccountId.newId(), money("100.00"), null));
    }

    @Test
    void of_throws_whenAmountZero() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> LedgerEntry.of(AccountId.newId(), money("0.00"), Direction.DEBIT));
    }

    @Test
    void of_throws_whenAmountNegative() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> LedgerEntry.of(AccountId.newId(), money("-100.00"), Direction.DEBIT));
    }

    @Test
    void equals_true_whenSameInstance() {
        LedgerEntry entry = LedgerEntry.of(AccountId.newId(), money("100.00"), Direction.DEBIT);
        assertEquals(entry, entry);
    }

    @Test
    void equals_false_whenDifferentId() {
        LedgerEntry entry1 = LedgerEntry.of(AccountId.newId(), money("100.00"), Direction.DEBIT);
        LedgerEntry entry2 = LedgerEntry.of(AccountId.newId(), money("100.00"), Direction.DEBIT);

        assertThat(entry1).isNotEqualTo(entry2);
    }

    @Test
    void equals_false_whenNull() {
        LedgerEntry entry = LedgerEntry.of(AccountId.newId(), money("100.00"), Direction.DEBIT);
        assertThat(entry.equals(null)).isFalse();
    }

    @Test
    void equals_false_whenDifferentType() {
        LedgerEntry entry = LedgerEntry.of(AccountId.newId(), money("100.00"), Direction.DEBIT);
        assertThat(entry.equals("x")).isFalse();
    }

    @Test
    void hashcode_equalsIdHashCode() {
        LedgerEntry entry = LedgerEntry.of(AccountId.newId(), money("100.00"), Direction.DEBIT);
        assertThat(entry.hashCode()).isEqualTo(entry.getId().hashCode());
    }


    private static Money money(String value) {
        return Money.of(new BigDecimal(value), TRY);
    }
}