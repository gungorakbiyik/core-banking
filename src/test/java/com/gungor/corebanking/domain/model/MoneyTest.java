package com.gungor.corebanking.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;

class MoneyTest {
    private static final Currency TRY = Currency.getInstance("TRY");
    private static final Currency USD = Currency.getInstance("USD");

    @Test
    void amount_isRoundedHalfEven_whenScaleExceedsCurrency() {
        Money money = money("10.005");
        assertThat(money).isEqualTo(money("10.00"));
    }

    @Test
    void amount_isRoundedUp_whenHalfPointIsTowardOddDigit() { // 10.015 -> 10.02
        Money money = money("10.015");
        assertThat(money).isEqualTo(money("10.02"));
    }

    @Test
    void amount_isPaddedWithZero_whenScaleIsBelowCurrency() { // 10.5 -> 10.50
        Money money = money("10.5");
        assertThat(money).isEqualTo(
                money("10.50")
        );
    }

    @Test
    void equals_isTrue_whenSameAmountAndCurrency() {
        Money money1 = money("10.00");
        Money money2 = money("10.00");
        assertThat(money1).isEqualTo(money2);
    }

    @Test
    void equals_isFalse_whenAmountDiffers() {
        Money money1 = money("10.00");
        Money money2 = money("10.01");
        assertThat(money1).isNotEqualTo(money2);
    }

    @Test
    void equals_isFalse_whenCurrencyDiffers() {
        Money money1 = money("10.00", USD);
        Money money2 = money("10.00", TRY);
        assertThat(money1).isNotEqualTo(money2);
    }

    @Test
    void equals_isFalse_whenComparedToNull() {
        Money money = money("10.00");
        assertThat(money).isNotEqualTo(null);
    }

    @Test
    void equals_isFalse_whenComparedToDifferentType() {
        Money money = money("10.00");
        assertThat(money).isNotEqualTo("10.00");
    }


    private static Money money(String amount) {
        return Money.of(new BigDecimal(amount), TRY);
    }

    private static Money money(String amount, Currency currency) {
        return Money.of(new BigDecimal(amount), currency);
    }
}