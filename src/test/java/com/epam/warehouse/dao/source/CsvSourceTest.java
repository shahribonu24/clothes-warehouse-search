package com.epam.warehouse.dao.source;

import com.epam.warehouse.TestData;
import com.epam.warehouse.dao.DaoException;
import com.epam.warehouse.dao.mapper.BootsMapper;
import com.epam.warehouse.dao.mapper.JacketMapper;
import com.epam.warehouse.entity.Boots;
import com.epam.warehouse.entity.Jacket;
import com.epam.warehouse.entity.attribute.ClothingSize;
import com.epam.warehouse.entity.attribute.Gender;
import com.epam.warehouse.entity.attribute.Season;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvSourceTest {

    private static <P> List<P> readAll(Source<P> source) throws DaoException, IOException {
        try (source) {
            source.init();
            List<P> result = new ArrayList<>();
            while (source.hasNext()) {
                result.add(source.next());
            }
            return result;
        }
    }

    @Test
    void readsAllRecordsSkippingCommentsAndBlankLines() throws Exception {
        List<Jacket> jackets = readAll(new CsvSource<>(TestData.JACKETS, new JacketMapper()));
        assertEquals(TestData.JACKETS_COUNT, jackets.size());

        Jacket expected = new Jacket().setId(1).setName("Storm Parka").setBrand("Columbia").setColor("Black")
                .setGender(Gender.MEN).setMaterial("Polyester").setPrice(new BigDecimal("149.99")).setQuantity(3)
                .setSize(ClothingSize.L).setSeason(Season.WINTER).setWaterproof(true).setHooded(true);
        assertEquals(expected, jackets.get(0));
    }

    @Test
    void mapsFootwearSpecificColumns() throws Exception {
        Boots boots = readAll(new CsvSource<>(TestData.BOOTS, new BootsMapper())).get(1);
        assertEquals(38.5, boots.getShoeSize());
        assertTrue(boots.isWaterproof());
        assertEquals(22, boots.getShaftHeight());
    }

    @Test
    void readsFromFileSystem(@TempDir Path dir) throws Exception {
        Path file = dir.resolve("boots.csv");
        Files.writeString(file, """
                id,name,brand,color,gender,material,price,quantity,shoe_size,waterproof,shaft_height
                7,Rain Boot,Hunter,Green,WOMEN,Rubber,99,4,39,yes,35
                """);
        List<Boots> boots = readAll(new CsvSource<>(file.toString(), new BootsMapper()));
        assertEquals(1, boots.size());
        assertEquals("Hunter", boots.get(0).getBrand());
    }

    @Test
    void columnsAreFoundByNameNotByPosition(@TempDir Path dir) throws Exception {
        Path file = dir.resolve("boots.csv");
        Files.writeString(file, """
                ID,Shaft_Height,Waterproof,Shoe_Size,Quantity,Price,Material,Gender,Color,Brand,Name
                7,35,no,39,4,99,Rubber,women,Green,Hunter,Rain Boot
                """);
        Boots boots = readAll(new CsvSource<>(file.toString(), new BootsMapper())).get(0);
        assertEquals("Rain Boot", boots.getName());
        assertEquals(35, boots.getShaftHeight());
    }

    @Test
    void missingFileCausesDaoException() {
        Source<Jacket> source = new CsvSource<>("test-data/no-such-file.csv", new JacketMapper());
        DaoException e = assertThrows(DaoException.class, source::init);
        assertTrue(e.getMessage().contains("no-such-file.csv"));
    }

    @Test
    void emptyFileCausesDaoException() {
        Source<Jacket> source = new CsvSource<>("test-data/empty.csv", new JacketMapper());
        assertThrows(DaoException.class, source::init);
    }

    @Test
    void invalidValueIsReportedWithLineNumber() throws Exception {
        try (Source<Jacket> source = new CsvSource<>("test-data/jackets-invalid-price.csv", new JacketMapper())) {
            source.init();
            source.next();
            DaoException e = assertThrows(DaoException.class, source::next);
            assertTrue(e.getMessage().contains("line 3"), e.getMessage());
            assertTrue(e.getMessage().contains("price"), e.getMessage());
        }
    }

    @Test
    void wrongNumberOfColumnsIsReported() throws Exception {
        try (Source<Jacket> source = new CsvSource<>("test-data/jackets-wrong-columns.csv", new JacketMapper())) {
            source.init();
            assertThrows(DaoException.class, source::next);
        }
    }

    @Test
    void nextAfterEndThrows() throws Exception {
        try (Source<Boots> source = new CsvSource<>(TestData.BOOTS, new BootsMapper())) {
            source.init();
            source.next();
            source.next();
            assertFalse(source.hasNext());
            assertThrows(NoSuchElementException.class, source::next);
        }
    }

    @Test
    void copyIsANewIndependentSource() throws Exception {
        Source<Boots> source = new CsvSource<>(TestData.BOOTS, new BootsMapper());
        Source<Boots> copy = source.copy();
        assertNotSame(source, copy);
        assertEquals(source.name(), copy.name());
        assertEquals(readAll(source), readAll(copy));
    }

    @Test
    void sourceCanNotBeInitializedTwice() throws Exception {
        try (Source<Boots> source = new CsvSource<>(TestData.BOOTS, new BootsMapper())) {
            source.init();
            assertThrows(IllegalStateException.class, source::init);
        }
    }
}
