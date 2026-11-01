package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.entity.Product;

public class ColorParameter extends TextParameter<Product<?>> {

    public ColorParameter(String color) {
        super("color", Product.class, Product::getColor, color, Mode.EQUALS);
    }
}
