package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.entity.Jacket;

import java.util.Set;

public class HoodedParameter extends ValueSetParameter<Jacket, Boolean> {

    public HoodedParameter(boolean hooded) {
        super("hooded", Jacket.class, Jacket::isHooded, Set.of(hooded));
    }
}
