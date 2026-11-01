package com.epam.warehouse.dao.mapper;

import com.epam.warehouse.dao.source.CsvRecord;
import com.epam.warehouse.entity.Jeans;
import com.epam.warehouse.entity.attribute.Fit;

public class JeansMapper extends ClothingMapper<Jeans> {

    private static final String WAIST = "waist";
    private static final String LENGTH = "length";
    private static final String FIT = "fit";

    @Override
    public Class<Jeans> type() {
        return Jeans.class;
    }

    @Override
    protected Jeans create() {
        return new Jeans();
    }

    @Override
    protected void fill(Jeans jeans, CsvRecord record) {
        super.fill(jeans, record);
        jeans.setWaist(record.getInt(WAIST))
                .setLength(record.getInt(LENGTH))
                .setFit(record.getEnum(FIT, Fit.class));
    }
}
