package com.epam.warehouse.dao.criteria.parameter;

import com.epam.warehouse.entity.Clothing;
import com.epam.warehouse.entity.attribute.ClothingSize;

import java.util.Collection;

public class SizeParameter extends ValueSetParameter<Clothing<?>, ClothingSize> {

    public SizeParameter(Collection<ClothingSize> sizes) {
        super("size", Clothing.class, Clothing::getSize, sizes);
    }
}
