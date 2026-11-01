package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.dao.criteria.Range;
import com.epam.warehouse.entity.Boots;

public class ShaftHeightParameter extends RangeParameter<Boots, Integer> {

    public ShaftHeightParameter(Range<Integer> range) {
        super("height", Boots.class, Boots::getShaftHeight, requireMin(range, 0, "Shaft height"));
    }
}
