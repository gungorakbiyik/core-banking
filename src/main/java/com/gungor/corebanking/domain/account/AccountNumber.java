package com.gungor.corebanking.domain.account;

import com.gungor.corebanking.common.Preconditions;

import java.math.BigInteger;

public record AccountNumber(String value) {
    private static final int TR_IBAN_LENGTH = 26;
    private static final String COUNTRY_CODE = "TR";
    private static final BigInteger NINETY_SEVEN = BigInteger.valueOf(97);

    public AccountNumber {
        Preconditions.notNull(value, "value");
        value = normalize(value);
        requireValidTrIban(value);
    }

    public static AccountNumber of(String value) {
        return new AccountNumber(value);
    }

    private static String normalize(String value) {
        return value.replace(" ", "").toUpperCase();
    }

    private static void requireValidTrIban(String iban) {
        if (iban.length() != TR_IBAN_LENGTH) {
            throw new IllegalArgumentException("IBAN length must be " + TR_IBAN_LENGTH + ": " + iban);
        }
        if (!iban.startsWith(COUNTRY_CODE)) {
            throw new IllegalArgumentException("IBAN must start with " + COUNTRY_CODE + ": " + iban);
        }
        if (mod97(iban) != 1) {
            throw new IllegalArgumentException("IBAN checksum invalid: " + iban);
        }
    }

    private static int mod97(String iban) {
        String rearranged = iban.substring(4) + iban.substring(0, 4);
        StringBuilder numeric = new StringBuilder();
        for (char c : rearranged.toCharArray()) {
            if (Character.isDigit(c)) {
                numeric.append(c);
            } else if (c >= 'A' && c <= 'Z') {
                numeric.append(c - 'A' + 10);
            } else {
                throw new IllegalArgumentException("IBAN has invalid character: " + c);
            }
        }

        return new BigInteger(numeric.toString()).mod(NINETY_SEVEN).intValue();
    }

    @Override
    public String toString() {
        return value;
    }
}
