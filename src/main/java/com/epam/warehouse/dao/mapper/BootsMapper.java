package com.epam.warehouse.dao.mapper;

import com.epam.warehouse.dao.source.CsvRecord;
import com.epam.warehouse.entity.Boots;

public class BootsMapper extends FootwearMapper<Boots> {

    private static final String WATERPROOF = "waterproof";
    private static final String SHAFT_HEIGHT = "shaft_height";

    @Override
    public Class<Boots> type() {
        return Boots.class;
    }

    @Override
    protected Boots create() {
        return new Boots();
    }

    @Override
    protected void fill(Boots boots, CsvRecord record) {
        super.fill(boots, record);
        boots.setWaterproof(record.getBoolean(WATERPROOF))
                .setShaftHeight(record.getInt(SHAFT_HEIGHT));
    }
}
