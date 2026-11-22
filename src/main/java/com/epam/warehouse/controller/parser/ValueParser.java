package com.epam.warehouse.controller.parser;

import com.epam.warehouse.dao.criteria.Range;
import com.epam.warehouse.util.EnumParser;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;

/**
 * Converts text values of request parameters. Throws {@link IllegalArgumentException}
 * with a user-friendly message on wrong input.
 * <ul>
 *     <li>range: {@code 50;150}, {@code ;100} (at most), {@code 10;} (at least), {@code 42} (exactly);</li>
 *     <li>enum set: {@code M,L} — comma-separated list;</li>
 *     <li>boolean: {@code true/false}, {@code yes/no}.</li>
 * </ul>
 */
public final class ValueParser {

    private static final String RANGE_SEPARATOR = ";";
    private static final String LIST_SEPARATOR = ",";

    private ValueParser() {
    }

    public static Range<BigDecimal> decimalRange(String text) {
        return range(text, ValueParser::decimal);
    }

    public static Range<Integer> intRange(String text) {
        return range(text, ValueParser::integer);
    }

    public static Range<Long> longRange(String text) {
        return range(text, s -> (long) integer(s));
    }

    public static Range<Double> doubleRange(String text) {
        return range(text, s -> decimal(s).doubleValue());
    }

    public static <E extends Enum<E>> Set<E> enumSet(Class<E> type, String text) {
        Set<E> values = new LinkedHashSet<>();
        for (String part : text.split(LIST_SEPARATOR, -1)) {
            values.add(EnumParser.parse(type, part));
        }
        return values;
    }

    public static boolean bool(String text) {
        switch (text.trim().toLowerCase(Locale.ROOT)) {
            case "true":
            case "yes":
                return true;
            case "false":
            case "no":
                return false;
            default:
                throw new IllegalArgumentException("Expected true or false, but was '" + text.trim() + "'");
        }
    }

    public static int positiveInt(String text) {
        int value = integer(text);
        if (value <= 0) {
            throw new IllegalArgumentException("Expected a positive number, but was " + value);
        }
        return value;
    }

    private static <T extends Comparable<? super T>> Range<T> range(String text, Function<String, T> converter) {
        String trimmed = text.trim();
        if (!trimmed.contains(RANGE_SEPARATOR)) {
            return Range.exactly(converter.apply(trimmed));
        }
        String[] borders = trimmed.split(RANGE_SEPARATOR, -1);
        if (borders.length != 2) {
            throw new IllegalArgumentException("Range must look like 'from;to', but was '" + trimmed + "'");
        }
        T from = borders[0].isBlank() ? null : converter.apply(borders[0].trim());
        T to = borders[1].isBlank() ? null : converter.apply(borders[1].trim());
        return new Range<>(from, to);
    }

    private static BigDecimal decimal(String text) {
        try {
            return new BigDecimal(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + text.trim() + "' is not a number", e);
        }
    }

    private static int integer(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + text.trim() + "' is not a whole number", e);
        }
    }

}
