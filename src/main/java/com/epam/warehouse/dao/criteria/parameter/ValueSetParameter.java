package com.epam.warehouse.dao.criteria.parameter;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

/**
 * Checks that an attribute equals one of the given values.
 */
public abstract class ValueSetParameter<P, V> extends AbstractParameter<P, V> {

    private final Set<V> values;

    protected ValueSetParameter(String name, Class<?> targetType, Function<? super P, ? extends V> extractor,
                                Collection<V> values) {
        super(name, targetType, extractor);
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("At least one value of '" + name + "' is required");
        }
        this.values = Set.copyOf(values);
    }

    @Override
    protected boolean matches(V value) {
        return values.contains(value);
    }

    @Override
    protected Object expected() {
        return values;
    }
}
