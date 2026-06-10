package com.gungor.corebanking.domain.account;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

class AccountNumberTest {
    private static final String VALID = "TR220006100000000000123456";

    @Test
    void of_acceptsValidTrIban() {
        assertThat(AccountNumber.of(VALID).value()).isEqualTo(VALID);
    }

    @Test
    void of_stripsSpaces_andStoresCanonicalForm() {
        AccountNumber number = AccountNumber.of("TR22 0006 1000 0000 0000 1234 56");
        assertThat(number.value()).isEqualTo(VALID);
    }

    @Test
    void of_uppercasesInput() {
        AccountNumber number = AccountNumber.of("tr220006100000000000123456");
        assertThat(number.value()).isEqualTo(VALID);
    }

    @Test
    void equals_isTrue_whenSameIban_regardlessOfFormatting() {
        assertThat(AccountNumber.of("TR22 0006 1000 0000 0000 1234 56"))
                .isEqualTo(AccountNumber.of(VALID));
    }

    @Test
    void of_throws_whenChecksumInvalid() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> AccountNumber.of("TR220006100000000000123457"))
                .withMessageContaining("checksum");
    }

    @Test
    void of_throws_whenLengthWrong() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> AccountNumber.of("TR2200061"))
                .withMessageContaining("length");
    }

    @Test
    void of_throws_whenCountryNotTr() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> AccountNumber.of("DE220006100000000000123456"))
                .withMessageContaining("TR");
    }

    @Test
    void of_throws_whenNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> AccountNumber.of(null))
                .withMessageContaining("value");
    }

    @Test
    void toString_returnsBareIban() {
        assertThat(AccountNumber.of(VALID).toString()).isEqualTo(VALID);
    }
}