package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.dao.criteria.Range;
import com.epam.warehouse.entity.Product;

public class IdParameter extends RangeParameter<Product<?>, Long> {

    public IdParameter(Range<Long> range) {
        super("id", Product.class, Product::getId, requireMin(range, 1L, "Id"));
    }
}
