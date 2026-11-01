package com.epam.warehouse.dao.criteria.parameter;

import java.util.Objects;
import java.util.function.Function;

/**
 * Base class of parameters that extract one attribute from a product and check it.
 *
 * @param <P> type of products to check
 * @param <V> type of the checked attribute
 */
public abstract class AbstractParameter<P, V> implements Parameter<P> {

    private final String name;
    private final Class<?> targetType;
    private final Function<? super P, ? extends V> extractor;

    protected AbstractParameter(String name, Class<?> targetType, Function<? super P, ? extends V> extractor) {
        this.name = Objects.requireNonNull(name);
        this.targetType = Objects.requireNonNull(targetType);
        this.extractor = Objects.requireNonNull(extractor);
    }

    @Override
    public final boolean test(P product) {
        if (product == null) {
            return false;
        }
        V value = extractor.apply(product);
        return value != null && matches(value);
    }

    protected abstract boolean matches(V value);

    /**
     * @return the expected value(s) — used for equality and string representation
     */
    protected abstract Object expected();

    @Override
    public String name() {
        return name;
    }

    @Override
    public Class<?> targetType() {
        return targetType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return expected().equals(((AbstractParameter<?, ?>) o).expected());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), expected());
    }

    @Override
    public String toString() {
        return name + "=" + expected();
    }
}
