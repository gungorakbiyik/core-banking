package com.gungor.corebanking.common;

public final class Preconditions {
    private Preconditions() {
        // utility class - no instantiation
    }

    public static <T> T notNull(T value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " cannot be null");
        }
        return value;
    }
}
