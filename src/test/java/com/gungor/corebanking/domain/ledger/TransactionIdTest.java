package com.gungor.corebanking.domain.ledger;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class TransactionIdTest {

    @Test
    void newId_generatesUniqueValues() {
        assertThat(TransactionId.newId()).isNotEqualTo(TransactionId.newId());
    }

    @Test
    void of_wrapsGivenUuid() {
        UUID uuid = UUID.randomUUID();
        assertThat(TransactionId.of(uuid).value()).isEqualTo(uuid);
    }

    @Test
    void equals_isTrue_whenSameUuid() {
        UUID uuid = UUID.randomUUID();
        assertThat(TransactionId.of(uuid)).isEqualTo(TransactionId.of(uuid));
    }

    @Test
    void equals_isFalse_whenDifferentUuid() {
        assertThat(TransactionId.of(UUID.randomUUID()))
                .isNotEqualTo(TransactionId.of(UUID.randomUUID()));
    }

    @Test
    void of_throws_whenUuidIsNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> TransactionId.of(null))
                .withMessageContaining("value");
    }

    @Test
    void toString_returnsBareUuid() {
        UUID uuid = UUID.randomUUID();
        assertThat(TransactionId.of(uuid).toString()).isEqualTo(uuid.toString());
    }

}