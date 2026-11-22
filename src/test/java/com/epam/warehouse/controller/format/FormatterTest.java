package com.epam.warehouse.controller.format;

import com.epam.warehouse.entity.Boots;
import com.epam.warehouse.entity.attribute.Gender;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FormatterTest {

    private static final String NL = System.lineSeparator();

    @Test
    void tableColumnsAreAligned() {
        String table = new TableFormatter(List.of("A", "Long header"))
                .addRow(List.of("longer cell", "x"))
                .format();
        assertEquals("A           | Long header" + NL
                + "-------------------------" + NL
                + "longer cell | x", table);
    }

    @Test
    void rowWithWrongSizeIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new TableFormatter(List.of("A")).addRow(List.of("1", "2")));
    }

    @Test
    void productDetailsShowSpecificAttributes() {
        Boots boots = new Boots().setId(1).setName("B").setBrand("B").setColor("Black").setGender(Gender.MEN)
                .setMaterial("Leather").setPrice(BigDecimal.ONE).setShoeSize(41).setWaterproof(true).setShaftHeight(15);
        assertEquals("shoeSize=41, waterproof=true, shaftHeight=15cm", ProductFormatter.details(boots));
    }

    @Test
    void emptyList() {
        assertEquals(ProductFormatter.NOTHING_FOUND, ProductFormatter.format(List.of()));
    }
}
