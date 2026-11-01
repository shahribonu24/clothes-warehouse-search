package com.epam.warehouse;

import com.epam.warehouse.dao.DaoFactory;
import com.epam.warehouse.dao.impl.ProductDaoImpl;
import com.epam.warehouse.dao.mapper.BootsMapper;
import com.epam.warehouse.dao.mapper.JacketMapper;
import com.epam.warehouse.dao.mapper.JeansMapper;
import com.epam.warehouse.dao.mapper.RecordMapper;
import com.epam.warehouse.dao.mapper.SneakersMapper;
import com.epam.warehouse.dao.source.CsvSource;

/**
 * Locations of test CSV files (src/test/resources/test-data) and registration of test DAOs.
 */
public final class TestData {

    public static final String JACKETS = "test-data/jackets-test.csv";
    public static final String JEANS = "test-data/jeans-test.csv";
    public static final String SNEAKERS = "test-data/sneakers-test.csv";
    public static final String BOOTS = "test-data/boots-test.csv";

    public static final int JACKETS_COUNT = 4;
    public static final int JEANS_COUNT = 3;
    public static final int SNEAKERS_COUNT = 3;
    public static final int BOOTS_COUNT = 2;
    public static final int TOTAL_COUNT = JACKETS_COUNT + JEANS_COUNT + SNEAKERS_COUNT + BOOTS_COUNT;

    private TestData() {
    }

    public static void registerTestDaos() {
        DaoFactory.clear();
        register(new JacketMapper(), JACKETS);
        register(new JeansMapper(), JEANS);
        register(new SneakersMapper(), SNEAKERS);
        register(new BootsMapper(), BOOTS);
    }

    private static <P> void register(RecordMapper<P> mapper, String location) {
        DaoFactory.register(mapper.type(), new ProductDaoImpl<>(new CsvSource<>(location, mapper)));
    }
}
