package com.epam.warehouse.service;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum SortDirection {
    ASC, DESC;

    public static Optional<SortDirection> byKey(String key) {
        if (key == null) {
            return Optional.empty();
        }
        String normalized = key.trim().toUpperCase(Locale.ROOT);
        return Arrays.stream(values()).filter(d -> d.name().equals(normalized)).findFirst();
    }
}
