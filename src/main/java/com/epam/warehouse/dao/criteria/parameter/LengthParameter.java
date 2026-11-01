package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.dao.criteria.Range;
import com.epam.warehouse.entity.Jeans;

public class LengthParameter extends RangeParameter<Jeans, Integer> {

    public LengthParameter(Range<Integer> range) {
        super("length", Jeans.class, Jeans::getLength, requireMin(range, 0, "Length"));
    }
}
