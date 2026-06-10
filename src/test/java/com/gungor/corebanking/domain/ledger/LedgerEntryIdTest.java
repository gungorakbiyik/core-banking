package com.gungor.corebanking.domain.ledger;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class LedgerEntryIdTest {

    @Test
    void newId_generatesUniqueValues() {
        assertThat(LedgerEntryId.newId()).isNotEqualTo(LedgerEntryId.newId());
    }

    @Test
    void of_wrapsGivenUuid() {
        UUID uuid = UUID.randomUUID();
        assertThat(LedgerEntryId.of(uuid).value()).isEqualTo(uuid);
    }

    @Test
    void equals_isTrue_whenSameUuid() {
        UUID uuid = UUID.randomUUID();
        assertThat(LedgerEntryId.of(uuid)).isEqualTo(LedgerEntryId.of(uuid));
    }

    @Test
    void equals_isFalse_whenDifferentUuid() {
        assertThat(LedgerEntryId.of(UUID.randomUUID()))
                .isNotEqualTo(LedgerEntryId.of(UUID.randomUUID()));
    }

    @Test
    void of_throws_whenUuidIsNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> LedgerEntryId.of(null))
                .withMessageContaining("value");
    }

    @Test
    void toString_returnsBareUuid() {
        UUID uuid = UUID.randomUUID();
        assertThat(LedgerEntryId.of(uuid).toString()).isEqualTo(uuid.toString());
    }

}