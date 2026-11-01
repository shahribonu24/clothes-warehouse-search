package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.dao.criteria.Range;
import com.epam.warehouse.entity.Footwear;

public class ShoeSizeParameter extends RangeParameter<Footwear<?>, Double> {

    public ShoeSizeParameter(Range<Double> range) {
        super("shoesize", Footwear.class, Footwear::getShoeSize, requireMin(range, 0.0, "Shoe size"));
    }
}
