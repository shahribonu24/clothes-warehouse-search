package com.epam.warehouse.dao.criteria;

import java.util.Objects;

/**
 * Closed interval of comparable values. A {@code null} border means "unbounded".
 */
public record Range<T extends Comparable<? super T>>(T from, T to) {

    public Range {
        if (from == null && to == null) {
            throw new IllegalArgumentException("At least one border of a range must be set");
        }
        if (from != null && to != null && from.compareTo(to) > 0) {
            throw new IllegalArgumentException("Lower border " + from + " is greater than upper border " + to);
        }
    }

    public static <T extends Comparable<? super T>> Range<T> exactly(T value) {
        return new Range<>(Objects.requireNonNull(value), value);
    }

    public static <T extends Comparable<? super T>> Range<T> atLeast(T from) {
        return new Range<>(Objects.requireNonNull(from), null);
    }

    public static <T extends Comparable<? super T>> Range<T> atMost(T to) {
        return new Range<>(null, Objects.requireNonNull(to));
    }

    public boolean contains(T value) {
        Objects.requireNonNull(value, "Value must not be null");
        boolean within = from == null || from.compareTo(value) <= 0;
        return within && (to == null || value.compareTo(to) <= 0);
    }

    @Override
    public String toString() {
        if (from != null && from.equals(to)) {
            return String.valueOf(from);
        }
        return (from == null ? "" : from) + ";" + (to == null ? "" : to);
    }
}
