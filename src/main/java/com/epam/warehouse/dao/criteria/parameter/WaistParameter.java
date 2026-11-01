package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.dao.criteria.Range;
import com.epam.warehouse.entity.Jeans;

public class WaistParameter extends RangeParameter<Jeans, Integer> {

    public WaistParameter(Range<Integer> range) {
        super("waist", Jeans.class, Jeans::getWaist, requireMin(range, 0, "Waist"));
    }
}
