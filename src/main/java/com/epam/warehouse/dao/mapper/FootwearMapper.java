package com.epam.warehouse.dao.mapper;

import com.epam.warehouse.dao.source.CsvRecord;
import com.epam.warehouse.entity.Footwear;

public abstract class FootwearMapper<P extends Footwear<P>> extends ProductMapper<P> {

    protected static final String SHOE_SIZE = "shoe_size";

    @Override
    protected void fill(P product, CsvRecord record) {
        super.fill(product, record);
        product.setShoeSize(record.getDouble(SHOE_SIZE));
    }
}
