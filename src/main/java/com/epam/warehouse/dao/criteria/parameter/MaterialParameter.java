package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.entity.Product;

/**
 * Matches products whose material contains the given text ("leather" matches "Suede leather").
 */
public class MaterialParameter extends TextParameter<Product<?>> {

    public MaterialParameter(String material) {
        super("material", Product.class, Product::getMaterial, material, Mode.CONTAINS);
    }
}
