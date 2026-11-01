package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.dao.criteria.Range;

import java.util.Objects;
import java.util.function.Function;

/**
 * Checks that a comparable attribute is within a range.
 */
public abstract class RangeParameter<P, V extends Comparable<? super V>> extends AbstractParameter<P, V> {

    private final Range<V> range;

    protected RangeParameter(String name, Class<?> targetType, Function<? super P, ? extends V> extractor,
                             Range<V> range) {
        super(name, targetType, extractor);
        this.range = Objects.requireNonNull(range, "Range must not be null");
    }

    public Range<V> range() {
        return range;
    }

    @Override
    protected boolean matches(V value) {
        return range.contains(value);
    }

    @Override
    protected Object expected() {
        return range;
    }

    /**
     * Helper for subclasses: both borders (if set) must not be less than the minimum.
     */
    protected static <V extends Comparable<? super V>> Range<V> requireMin(Range<V> range, V min, String name) {
        Objects.requireNonNull(range, "Range must not be null");
        if ((range.from() != null && range.from().compareTo(min) < 0)
                || (range.to() != null && range.to().compareTo(min) < 0)) {
            throw new IllegalArgumentException(name + " can't be less than " + min + ", but was " + range);
        }
        return range;
    }
}
