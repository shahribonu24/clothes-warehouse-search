package com.epam.warehouse.dao.source;

import com.epam.warehouse.entity.attribute.Season;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvRecordTest {

    private final CsvRecord record = CsvRecord.of(
            List.of("id", "price", "size", "season", "flag", "empty"),
            List.of("12", "19.90", "42.5", "demi-season", "yes", ""));

    @Test
    void typedGetters() {
        assertEquals(12L, record.getLong("id"));
        assertEquals(12, record.getInt("id"));
        assertEquals(new BigDecimal("19.90"), record.getDecimal("price"));
        assertEquals(42.5, record.getDouble("size"));
        assertEquals(Season.DEMI_SEASON, record.getEnum("season", Season.class));
        assertTrue(record.getBoolean("flag"));
    }

    @Test
    void invalidValuesThrowDataFormatException() {
        assertThrows(DataFormatException.class, () -> record.getInt("price"));
        assertThrows(DataFormatException.class, () -> record.getDecimal("season"));
        assertThrows(DataFormatException.class, () -> record.getBoolean("id"));
        assertThrows(DataFormatException.class, () -> record.getEnum("flag", Season.class));
        assertThrows(DataFormatException.class, () -> record.getString("empty"));
        assertThrows(DataFormatException.class, () -> record.getString("unknown"));
    }

    @Test
    void falseValues() {
        CsvRecord r = CsvRecord.of(List.of("a", "b"), List.of("false", "NO"));
        assertFalse(r.getBoolean("a"));
        assertFalse(r.getBoolean("b"));
    }

    @Test
    void headerAndValuesMustHaveSameSize() {
        assertThrows(DataFormatException.class, () -> CsvRecord.of(List.of("a", "b"), List.of("1")));
    }
}
