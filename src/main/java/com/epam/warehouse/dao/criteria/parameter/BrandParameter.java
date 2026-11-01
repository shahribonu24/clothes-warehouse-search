package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.entity.Product;

public class BrandParameter extends TextParameter<Product<?>> {

    public BrandParameter(String brand) {
        super("brand", Product.class, Product::getBrand, brand, Mode.EQUALS);
    }
}
