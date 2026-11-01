package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.entity.Jeans;
import com.epam.warehouse.entity.attribute.Fit;

import java.util.Collection;

public class FitParameter extends ValueSetParameter<Jeans, Fit> {

    public FitParameter(Collection<Fit> fits) {
        super("fit", Jeans.class, Jeans::getFit, fits);
    }
}
