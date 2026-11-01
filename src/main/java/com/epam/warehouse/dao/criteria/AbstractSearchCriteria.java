package com.epam.warehouse.dao.criteria;

import com.epam.warehouse.dao.criteria.parameter.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractSearchCriteria<P> implements SearchCriteria<P> {

    private final Class<P> productType;
    private final List<Parameter<? super P>> parameters = new ArrayList<>();

    protected AbstractSearchCriteria(Class<P> productType) {
        this.productType = Objects.requireNonNull(productType);
    }

    @Override
    public Class<P> productType() {
        return productType;
    }

    @Override
    public SearchCriteria<P> add(Parameter<? super P> parameter) {
        parameters.add(Objects.requireNonNull(parameter, "Parameter must not be null"));
        return this;
    }

    @Override
    public SearchCriteria<P> addChecked(Parameter<?> parameter) {
        Objects.requireNonNull(parameter, "Parameter must not be null");
        if (!parameter.isApplicableTo(productType)) {
            throw new IllegalArgumentException("Parameter '" + parameter.name() + "' is not applicable to "
                    + productType.getSimpleName());
        }
        // safe: the parameter's target type is a supertype of P (checked above)
        @SuppressWarnings("unchecked")
        Parameter<? super P> typed = (Parameter<? super P>) parameter;
        return add(typed);
    }

    @Override
    public boolean test(P product) {
        return product != null && parameters.stream().allMatch(parameter -> parameter.test(product));
    }

    @Override
    public List<Parameter<? super P>> parameters() {
        return List.copyOf(parameters);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + parameters;
    }
}
