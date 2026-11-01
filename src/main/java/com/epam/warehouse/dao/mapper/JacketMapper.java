package com.epam.warehouse.dao.mapper;

import com.epam.warehouse.dao.source.CsvRecord;
import com.epam.warehouse.entity.Jacket;

public class JacketMapper extends ClothingMapper<Jacket> {

    private static final String WATERPROOF = "waterproof";
    private static final String HOODED = "hooded";

    @Override
    public Class<Jacket> type() {
        return Jacket.class;
    }

    @Override
    protected Jacket create() {
        return new Jacket();
    }

    @Override
    protected void fill(Jacket jacket, CsvRecord record) {
        super.fill(jacket, record);
        jacket.setWaterproof(record.getBoolean(WATERPROOF))
                .setHooded(record.getBoolean(HOODED));
    }
}
