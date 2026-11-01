package com.epam.warehouse.dao.source;

import com.epam.warehouse.util.EnumParser;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * One data line of a CSV file with typed access to its columns by header name.
 */
public final class CsvRecord {

    private final Map<String, String> values;

    private CsvRecord(Map<String, String> values) {
        this.values = values;
    }

    public static CsvRecord of(List<String> header, List<String> fields) {
        if (header.size() != fields.size()) {
            throw new DataFormatException("Expected " + header.size() + " values but found " + fields.size());
        }
        Map<String, String> values = new LinkedHashMap<>();
        for (int i = 0; i < header.size(); i++) {
            values.put(header.get(i), fields.get(i));
        }
        return new CsvRecord(Collections.unmodifiableMap(values));
    }

    public String getString(String column) {
        if (!values.containsKey(column)) {
            throw new DataFormatException("Missing column '" + column + "'");
        }
        String value = values.get(column);
        if (value.isBlank()) {
            throw new DataFormatException("Column '" + column + "' is empty");
        }
        return value;
    }

    public long getLong(String column) {
        String value = getString(column);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw invalid(column, value, "an integer number");
        }
    }

    public int getInt(String column) {
        String value = getString(column);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw invalid(column, value, "an integer number");
        }
    }

    public double getDouble(String column) {
        String value = getString(column);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw invalid(column, value, "a number");
        }
    }

    public BigDecimal getDecimal(String column) {
        String value = getString(column);
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw invalid(column, value, "a decimal number");
        }
    }

    public boolean getBoolean(String column) {
        String value = getString(column).toLowerCase(Locale.ROOT);
        if ("true".equals(value) || "yes".equals(value)) {
            return true;
        }
        if ("false".equals(value) || "no".equals(value)) {
            return false;
        }
        throw invalid(column, value, "true/false");
    }

    public <E extends Enum<E>> E getEnum(String column, Class<E> type) {
        String value = getString(column);
        try {
            return EnumParser.parse(type, value);
        } catch (IllegalArgumentException e) {
            throw new DataFormatException("Column '" + column + "': " + e.getMessage());
        }
    }

    private static DataFormatException invalid(String column, String value, String expected) {
        return new DataFormatException("Column '" + column + "': '" + value + "' is not " + expected);
    }
}
