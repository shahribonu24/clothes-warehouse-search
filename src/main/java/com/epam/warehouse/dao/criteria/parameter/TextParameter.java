package com.epam.warehouse.dao.criteria.parameter;

import java.util.Locale;
import java.util.function.Function;

/**
 * Case-insensitive comparison of a text attribute.
 */
public abstract class TextParameter<P> extends AbstractParameter<P, String> {

    public enum Mode {
        EQUALS, CONTAINS
    }

    private final String text;
    private final Mode mode;

    protected TextParameter(String name, Class<?> targetType, Function<? super P, String> extractor,
                            String text, Mode mode) {
        super(name, targetType, extractor);
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Value of '" + name + "' must not be empty");
        }
        this.text = text.trim().toLowerCase(Locale.ROOT);
        this.mode = mode;
    }

    @Override
    protected boolean matches(String value) {
        String normalized = value.toLowerCase(Locale.ROOT);
        return mode == Mode.EQUALS ? normalized.equals(text) : normalized.contains(text);
    }

    @Override
    protected Object expected() {
        return text;
    }
}
