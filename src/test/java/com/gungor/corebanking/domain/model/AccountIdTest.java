package com.gungor.corebanking.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

class AccountIdTest {

    @Test
    void newId_generatesUniqueValues() {
        assertThat(AccountId.newId()).isNotEqualTo(AccountId.newId());
    }

    @Test
    void of_wrapsGivenUuid() {
        UUID uuid = UUID.randomUUID();
        assertThat(AccountId.of(uuid).value()).isEqualTo(uuid);
    }

    @Test
    void equals_isTrue_whenSameUuid() {
        UUID uuid = UUID.randomUUID();
        assertThat(AccountId.of(uuid)).isEqualTo(AccountId.of(uuid));
    }

    @Test
    void equals_isFalse_whenDifferentUuid() {
        assertThat(AccountId.of(UUID.randomUUID()))
                .isNotEqualTo(AccountId.of(UUID.randomUUID()));
    }

    @Test
    void of_throws_whenUuidIsNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> AccountId.of(null))
                .withMessageContaining("value");
    }

    @Test
    void toString_returnsBareUuid() {
        UUID uuid = UUID.randomUUID();
        assertThat(AccountId.of(uuid).toString()).isEqualTo(uuid.toString());
    }

}