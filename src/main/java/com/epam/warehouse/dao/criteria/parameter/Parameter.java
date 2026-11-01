package com.epam.warehouse.dao.criteria.parameter;

/**
 * A single search condition.
 *
 * @param <P> the most general type of products the parameter can check
 */
public interface Parameter<P> {

    boolean test(P product);

    /**
     * @return the most general product type this parameter can be applied to
     */
    Class<?> targetType();

    /**
     * @return parameter name as used in search requests
     */
    String name();

    default boolean isApplicableTo(Class<?> productType) {
        return targetType().isAssignableFrom(productType);
    }

    static <P> Parameter<P> any() {
        return new ConstantParameter<>(true);
    }

    static <P> Parameter<P> none() {
        return new ConstantParameter<>(false);
    }
}
