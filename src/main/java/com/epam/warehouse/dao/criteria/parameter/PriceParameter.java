package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.dao.criteria.Range;
import com.epam.warehouse.entity.Product;

import java.math.BigDecimal;

public class PriceParameter extends RangeParameter<Product<?>, BigDecimal> {

    public PriceParameter(Range<BigDecimal> range) {
        super("price", Product.class, Product::getPrice, requireMin(range, BigDecimal.ZERO, "Price"));
    }
}
