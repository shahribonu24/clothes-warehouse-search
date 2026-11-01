package com.epam.warehouse.dao.mapper;

import com.epam.warehouse.dao.source.CsvRecord;
import com.epam.warehouse.entity.Clothing;
import com.epam.warehouse.entity.attribute.ClothingSize;
import com.epam.warehouse.entity.attribute.Season;

public abstract class ClothingMapper<P extends Clothing<P>> extends ProductMapper<P> {

    protected static final String SIZE = "size";
    protected static final String SEASON = "season";

    @Override
    protected void fill(P product, CsvRecord record) {
        super.fill(product, record);
        product.setSize(record.getEnum(SIZE, ClothingSize.class))
                .setSeason(record.getEnum(SEASON, Season.class));
    }
}
