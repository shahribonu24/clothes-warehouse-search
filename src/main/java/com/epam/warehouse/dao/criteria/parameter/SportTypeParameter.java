package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.entity.Sneakers;
import com.epam.warehouse.entity.attribute.SportType;

import java.util.Collection;

public class SportTypeParameter extends ValueSetParameter<Sneakers, SportType> {

    public SportTypeParameter(Collection<SportType> sportTypes) {
        super("sport", Sneakers.class, Sneakers::getSportType, sportTypes);
    }
}
