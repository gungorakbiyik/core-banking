package com.gungor.corebanking.domain.ledger;

import com.gungor.corebanking.domain.account.AccountId;
import com.gungor.corebanking.domain.shared.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TransactionTest {

    private static final Currency TRY = Currency.getInstance("TRY");
    private static final Currency USD = Currency.getInstance("USD");

    // --- transfer (public) ---

    @Test
    void transfer_createsTwoEntryTransaction() {
        Transaction tx = Transaction.transfer(
                AccountId.newId(),
                AccountId.newId(),
                money("100.00", TRY)
        );
        assertThat(tx.getEntries()).hasSize(2);
    }

    @Test
    void transfer_debitsFromAndCreditsTo() {
        AccountId from = AccountId.newId();
        AccountId to = AccountId.newId();
        Money amount = money("100.00", TRY);

        Transaction tx = Transaction.transfer(from, to, amount);

        LedgerEntry debit = entryWithDirection(tx, Direction.DEBIT);
        LedgerEntry credit = entryWithDirection(tx, Direction.CREDIT);

        assertThat(debit.getAccountId()).isEqualTo(from);
        assertThat(credit.getAccountId()).isEqualTo(to);
        assertThat(debit.getAmount()).isEqualTo(amount);
        assertThat(credit.getAmount()).isEqualTo(amount);
    }

    @Test
    void transfer_generatesIdAndTimestamp() {
        Transaction tx = Transaction.transfer(AccountId.newId(), AccountId.newId(), money("100.00", TRY));
        assertThat(tx.getId()).isNotNull();
        assertThat(tx.getOccurredAt()).isNotNull();
    }

    @Test
    void getEntries_isImmutable() {
        Transaction tx = Transaction.transfer(
                AccountId.newId(),
                AccountId.newId(),
                money("100.00", TRY)
        );

        assertThatThrownBy(() -> tx.getEntries().add(entry(Direction.DEBIT, "100.00", TRY)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    // --- equals / hashCode (id-bazlı) ---

    @Test
    void equals_true_whenSameInstance() {
        Transaction tx = Transaction.transfer(AccountId.newId(), AccountId.newId(), money("100.00", TRY));
        assertThat(tx).isEqualTo(tx);
    }

    @Test
    void equals_false_whenDifferentId() {
        Transaction tx1 = Transaction.transfer(AccountId.newId(), AccountId.newId(), money("100.00", TRY));
        Transaction tx2 = Transaction.transfer(AccountId.newId(), AccountId.newId(), money("100.00", TRY));
        assertThat(tx1).isNotEqualTo(tx2);
    }

    @Test
    void equals_false_whenNull() {
        Transaction tx = Transaction.transfer(AccountId.newId(), AccountId.newId(), money("100.00", TRY));
        assertThat(tx.equals(null)).isFalse();
    }

    @Test
    void equals_false_whenDifferentType() {
        Transaction tx = Transaction.transfer(AccountId.newId(), AccountId.newId(), money("100.00", TRY));
        assertThat(tx.equals("x")).isFalse();
    }

    @Test
    void hashcode_equalsIdHashCode() {
        Transaction tx = Transaction.transfer(AccountId.newId(), AccountId.newId(), money("100.00", TRY));
        assertThat(tx.hashCode()).isEqualTo(tx.getId().hashCode());
    }

    // --- invariant guard'ları (package-private of ile) ---

    @Test
    void of_throws_whenUnbalanced() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            Transaction.of(List.of(
                entry(Direction.DEBIT, "100.00", TRY),
                entry(Direction.CREDIT, "90.00", TRY)
            ));
        });
    }

    @Test
    void of_throws_whenFewerThanTwoEntries() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            Transaction.of(List.of(
                entry(Direction.DEBIT, "100.00", TRY)
            ));
        });
    }

    @Test
    void of_throws_whenMixedCurrency() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            Transaction.of(List.of(
                entry(Direction.DEBIT, "100.00", TRY),
                entry(Direction.CREDIT, "100.00", USD)
            ));
        });
    }

    @Test
    void of_throws_whenNoCreditEntry() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            Transaction.of(List.of(
                entry(Direction.DEBIT, "100.00", TRY),
                entry(Direction.DEBIT, "100.00", TRY)
            ));
        });
    }

    @Test
    void of_throws_whenEntriesNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            Transaction.of(null);
        });
    }

    @Test
    void of_throws_whenContainsNull() {
        List<LedgerEntry> entries = Arrays.asList(entry(Direction.DEBIT, "100.00", TRY), null);
        assertThatIllegalArgumentException().isThrownBy(() -> {
            Transaction.of(entries);
        });
    }

    // --- helpers ---

    private static Money money(String value, Currency currency) {
        return Money.of(new BigDecimal(value), currency);
    }

    private static LedgerEntry entry(Direction direction, String amount, Currency currency) {
        return LedgerEntry.of(AccountId.newId(), money(amount, currency), direction);
    }

    private static LedgerEntry entryWithDirection(Transaction tx, Direction direction) {
        return tx.getEntries().stream()
                .filter(e -> e.getDirection() == direction)
                .findFirst()
                .orElseThrow();
    }

}