package com.epam.warehouse.dao.mapper;

import com.epam.warehouse.dao.source.CsvRecord;

/**
 * Converts a raw data record into a domain object.
 */
public interface RecordMapper<P> {

    P map(CsvRecord record);

    /**
     * @return the type of objects created by this mapper
     */
    Class<P> type();
}
