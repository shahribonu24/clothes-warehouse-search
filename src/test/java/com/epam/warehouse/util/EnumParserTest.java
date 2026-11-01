package com.epam.warehouse.util;

import com.epam.warehouse.entity.attribute.Season;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnumParserTest {

    @ParameterizedTest
    @ValueSource(strings = {"DEMI_SEASON", "demi-season", " Demi Season "})
    void parsesTolerantly(String text) {
        assertEquals(Season.DEMI_SEASON, EnumParser.parse(Season.class, text));
    }

    @Test
    void unknownValueMessageListsAllowedValues() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> EnumParser.parse(Season.class, "spring"));
        assertTrue(e.getMessage().contains("WINTER, SUMMER, DEMI_SEASON, ALL_SEASON"));
    }

    @Test
    void emptyValueIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> EnumParser.parse(Season.class, " "));
    }
}
