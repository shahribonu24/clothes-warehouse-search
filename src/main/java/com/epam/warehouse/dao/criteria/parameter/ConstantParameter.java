package com.epam.warehouse.dao.criteria.parameter;

/**
 * Parameter that is satisfied by every product ({@code any}) or by none ({@code none}).
 */
record ConstantParameter<P>(boolean result) implements Parameter<P> {

    @Override
    public boolean test(P product) {
        return result;
    }

    @Override
    public Class<?> targetType() {
        return Object.class;
    }

    @Override
    public String name() {
        return result ? "any" : "none";
    }
}
