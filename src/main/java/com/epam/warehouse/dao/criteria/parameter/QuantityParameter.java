package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.dao.criteria.Range;
import com.epam.warehouse.entity.Product;

public class QuantityParameter extends RangeParameter<Product<?>, Integer> {

    public QuantityParameter(Range<Integer> range) {
        super("quantity", Product.class, Product::getQuantity, requireMin(range, 0, "Quantity"));
    }
}
