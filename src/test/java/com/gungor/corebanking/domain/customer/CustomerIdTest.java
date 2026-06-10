package com.gungor.corebanking.domain.customer;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class CustomerIdTest {

    @Test
    void newId_generatesUniqueValues() {
        assertThat(CustomerId.newId()).isNotEqualTo(CustomerId.newId());
    }

    @Test
    void of_wrapsGivenUuid() {
        UUID uuid = UUID.randomUUID();
        assertThat(CustomerId.of(uuid).value()).isEqualTo(uuid);
    }

    @Test
    void equals_isTrue_whenSameUuid() {
        UUID uuid = UUID.randomUUID();
        assertThat(CustomerId.of(uuid)).isEqualTo(CustomerId.of(uuid));
    }

    @Test
    void equals_isFalse_whenDifferentUuid() {
        assertThat(CustomerId.of(UUID.randomUUID()))
                .isNotEqualTo(CustomerId.of(UUID.randomUUID()));
    }

    @Test
    void of_throws_whenUuidIsNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> CustomerId.of(null))
                .withMessageContaining("value");
    }

    @Test
    void toString_returnsBareUuid() {
        UUID uuid = UUID.randomUUID();
        assertThat(CustomerId.of(uuid).toString()).isEqualTo(uuid.toString());
    }

}