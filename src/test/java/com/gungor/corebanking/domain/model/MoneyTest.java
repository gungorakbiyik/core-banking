package com.gungor.corebanking.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

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

    @Test
    void plus_addsAmounts_whenSameCurrency() {
        assertThat(money("10.00").plus(money("5.50")))
                .isEqualTo(money("15.50"));
    }

    @Test
    void minus_subtractsAmounts_whenSameCurrency() {
        assertThat(money("10.00").minus(money("3.00")))
                .isEqualTo(money("7.00"));
    }

    @Test
    void minus_canProduceNegativeMoney() { // kasıtlı: pozitiflik kısıtı Money'de değil, LedgerEntry'de yaşayacak
        assertThat(money("5.00").minus(money("8.00")))
                .isEqualTo(money("-3.00"));
    }

    @Test
    void multiply_scalesAmount() {
        assertThat(money("10.00").multiply(new BigDecimal("3")))
                .isEqualTo(money("30.00"));
    }

    @Test
    void multiply_roundsResultToCurrencyScale() { // 0.05 × 0.5 = 0.025 -> HALF_EVEN -> 0.02; scale'i of()'a bırakır
        assertThat(money("0.05").multiply(new BigDecimal("0.5")))
                .isEqualTo(money("0.02"));
    }

    // --- currency safety ---

    @Test
    void plus_throws_whenCurrencyDiffers() {
        Money inUsd = money("10.00", USD);
        Money inTry = money("10.00", TRY);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> inUsd.plus(inTry))
                .withMessageContaining("currency mismatch");
    }

    @Test
    void minus_throws_whenCurrencyDiffers() {
        Money inUsd = money("10.00", USD);
        Money inTry = money("10.00", TRY);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> inUsd.minus(inTry));
    }

    // --- sign queries ---

    @Test
    void isPositive_isTrue_whenAmountAboveZero() {
        assertThat(money("0.01").isPositive()).isTrue();
    }

    @Test
    void isNegative_isTrue_whenAmountBelowZero() {
        assertThat(money("-0.01").isNegative()).isTrue();
    }

    @Test
    void zero_isNeitherPositiveNorNegative() {
        Money zero = money("0.00");
        assertThat(zero.isZero()).isTrue();
        assertThat(zero.isPositive()).isFalse();
        assertThat(zero.isNegative()).isFalse();
    }

    // --- null guards ---

    @Test
    void of_throws_whenAmountIsNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Money.of(null, TRY))
                .withMessageContaining("amount");
    }

    @Test
    void of_throws_whenCurrencyIsNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Money.of(new BigDecimal("10.00"), null))
                .withMessageContaining("currency");
    }

    @Test
    void plus_throws_whenOtherIsNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> money("10.00").plus(null));
    }

    // --- toString ---

    @Test
    void toString_showsAmountAndCurrencyCode() {
        assertThat(money("10.50").toString()).isEqualTo("10.50 TRY"); // scale korunur: 10.5 değil 10.50
    }

    private static Money money(String amount) {
        return Money.of(new BigDecimal(amount), TRY);
    }

    private static Money money(String amount, Currency currency) {
        return Money.of(new BigDecimal(amount), currency);
    }
}