package com.epam.warehouse.util;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Converts user / file text into enum constants in a tolerant way:
 * case-insensitive, and both '-' and ' ' are treated as '_' ("demi-season" → DEMI_SEASON).
 */
public final class EnumParser {

    private EnumParser() {
    }

    public static <E extends Enum<E>> E parse(Class<E> type, String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(type.getSimpleName() + " value is empty");
        }
        String normalized = text.trim().replace('-', '_').replace(' ', '_').toUpperCase(Locale.ROOT);
        for (E constant : type.getEnumConstants()) {
            if (constant.name().equals(normalized)) {
                return constant;
            }
        }
        throw new IllegalArgumentException("Unknown " + type.getSimpleName() + " '" + text.trim()
                + "'. Allowed values: " + allowedValues(type));
    }

    public static <E extends Enum<E>> String allowedValues(Class<E> type) {
        return Arrays.stream(type.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
