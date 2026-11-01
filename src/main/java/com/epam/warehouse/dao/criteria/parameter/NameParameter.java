package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.entity.Product;

/**
 * Matches products whose name contains the given text.
 */
public class NameParameter extends TextParameter<Product<?>> {

    public NameParameter(String text) {
        super("name", Product.class, Product::getName, text, Mode.CONTAINS);
    }
}
