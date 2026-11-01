package com.epam.warehouse.dao.criteria;

import com.epam.warehouse.dao.criteria.parameter.Parameter;

import java.util.List;

/**
 * A set of search parameters for products of one type. A product matches the criteria
 * when it satisfies every parameter (an empty criteria matches everything).
 *
 * @param <P> product type
 */
public interface SearchCriteria<P> {

    Class<P> productType();

    SearchCriteria<P> add(Parameter<? super P> parameter);

    /**
     * Adds a parameter whose static type is unknown (e.g. created from user input),
     * checking at runtime that it can be applied to {@link #productType()}.
     *
     * @throws IllegalArgumentException if the parameter is not applicable
     */
    SearchCriteria<P> addChecked(Parameter<?> parameter);

    boolean test(P product);

    List<Parameter<? super P>> parameters();
}
