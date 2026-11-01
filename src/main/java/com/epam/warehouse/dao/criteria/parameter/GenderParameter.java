package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.entity.Product;
import com.epam.warehouse.entity.attribute.Gender;

import java.util.Collection;

public class GenderParameter extends ValueSetParameter<Product<?>, Gender> {

    public GenderParameter(Collection<Gender> genders) {
        super("gender", Product.class, Product::getGender, genders);
    }
}
