package com.epam.warehouse.controller.parser;

import com.epam.warehouse.dao.criteria.Range;
import com.epam.warehouse.entity.attribute.ClothingSize;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValueParserTest {

    @Test
    void ranges() {
        assertEquals(new Range<>(new BigDecimal("50"), new BigDecimal("150.5")), ValueParser.decimalRange("50;150.5"));
        assertEquals(Range.atMost(100), ValueParser.intRange(";100"));
        assertEquals(Range.atLeast(1L), ValueParser.longRange(" 1; "));
        assertEquals(Range.exactly(42.5), ValueParser.doubleRange("42.5"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "1;2;3", ";", "10;5", "1.5"})
    void wrongIntRangeIsRejected(String text) {
        assertThrows(IllegalArgumentException.class, () -> ValueParser.intRange(text));
    }

    @Test
    void enumSetKeepsOrderAndIsCaseInsensitive() {
        assertEquals(List.of(ClothingSize.M, ClothingSize.XL),
                List.copyOf(ValueParser.enumSet(ClothingSize.class, "m, XL,m")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"M,XXXL", "M,", ""})
    void wrongEnumValueIsRejected(String text) {
        assertThrows(IllegalArgumentException.class, () -> ValueParser.enumSet(ClothingSize.class, text));
    }

    @Test
    void booleans() {
        assertTrue(ValueParser.bool("TRUE"));
        assertTrue(ValueParser.bool("yes"));
        assertFalse(ValueParser.bool(" false "));
        assertFalse(ValueParser.bool("No"));
        assertThrows(IllegalArgumentException.class, () -> ValueParser.bool("maybe"));
    }

    @Test
    void positiveInt() {
        assertEquals(3, ValueParser.positiveInt("3"));
        assertThrows(IllegalArgumentException.class, () -> ValueParser.positiveInt("0"));
        assertThrows(IllegalArgumentException.class, () -> ValueParser.positiveInt("-2"));
        assertThrows(IllegalArgumentException.class, () -> ValueParser.positiveInt("two"));
    }
}
