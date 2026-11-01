package com.epam.warehouse.dao.mapper;

import com.epam.warehouse.dao.source.CsvRecord;
import com.epam.warehouse.entity.Sneakers;
import com.epam.warehouse.entity.attribute.SportType;

public class SneakersMapper extends FootwearMapper<Sneakers> {

    private static final String SPORT = "sport";

    @Override
    public Class<Sneakers> type() {
        return Sneakers.class;
    }

    @Override
    protected Sneakers create() {
        return new Sneakers();
    }

    @Override
    protected void fill(Sneakers sneakers, CsvRecord record) {
        super.fill(sneakers, record);
        sneakers.setSportType(record.getEnum(SPORT, SportType.class));
    }
}
