package com.gungor.corebanking.domain.account;

import com.gungor.corebanking.domain.customer.CustomerId;
import org.junit.jupiter.api.Test;

import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void open_setsStatusToActive() {
        Account account = createAccountWithDefaults();
        assertEquals(AccountStatus.ACTIVE, account.getStatus());
    }

    @Test
    void open_setsGivenFields() {
        CustomerId customerId = CustomerId.newId();
        AccountType accountType = AccountType.CHECKING;
        Currency currency = Currency.getInstance("TRY");

        Account account = Account.open(customerId, accountType, currency);

        assertEquals(customerId, account.getCustomerId());
        assertEquals(accountType, account.getType());
        assertEquals(currency, account.getCurrency());
    }

    @Test
    void open_generatesNonNullUniqueId() {
        Account account1 = createAccountWithDefaults();
        Account account2 = createAccountWithDefaults();

        assertNotNull(account1.getId());
        assertNotNull(account2.getId());

        assertNotEquals(account1.getId(), account2.getId());
    }

    @Test
    void open_setsOpenedAt() {
        Account account = createAccountWithDefaults();
        assertNotNull(account.getOpenedAt());
    }

    @Test
    void open_throws_whenCustomerIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> createAccountWithId(null)
        );
    }

    @Test
    void equals_true_whenSameInstance() {
        Account account = createAccountWithDefaults();
        assertEquals(account, account);
    }

    @Test
    void equals_false_whenDifferentId() {
        Account account1 = createAccountWithDefaults();
        Account account2 = createAccountWithDefaults();

        assertThat(account1).isNotEqualTo(account2);
    }

    @Test
    void equals_false_whenNull() {
        Account account = createAccountWithDefaults();
        assertThat(account.equals(null)).isFalse();
    }

    @Test
    void equals_false_whenDifferentType() {
        Account account = createAccountWithDefaults();
        assertThat(account.equals("x")).isFalse();
    }

    @Test
    void hashcode_equalsIdHashCode() {
        Account account = createAccountWithDefaults();
        assertThat(account.hashCode()).isEqualTo(account.getId().hashCode());
    }

    @Test
    void freeze_setsStatusToFrozen() {
        Account account = createAccountWithDefaults();
        account.freeze();
        assertEquals(AccountStatus.FROZEN, account.getStatus());
    }

    @Test
    void freeze_throws_whenAlreadyFrozen() {
        Account account = createAccountWithDefaults();
        account.freeze();
        assertThatIllegalArgumentException().isThrownBy(account::freeze);
    }

    private Account createAccountWithDefaults() {
        return Account.open(CustomerId.newId(),
                AccountType.CHECKING,
                Currency.getInstance("TRY")
        );
    }

    private Account createAccountWithId(CustomerId customerId) {
        return Account.open(customerId,
                AccountType.CHECKING,
                Currency.getInstance("TRY")
        );
    }


}