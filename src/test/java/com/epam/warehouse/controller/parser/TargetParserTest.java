package com.epam.warehouse.controller.parser;

import com.epam.warehouse.controller.ControllerException;
import com.epam.warehouse.entity.Category;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TargetParserTest {

    @Test
    void groupsAndSingleCategories() throws ControllerException {
        assertEquals(EnumSet.allOf(Category.class), TargetParser.parse("all"));
        assertEquals(EnumSet.allOf(Category.class), TargetParser.parse(null));
        assertEquals(EnumSet.of(Category.JACKETS, Category.JEANS), TargetParser.parse("Clothing"));
        assertEquals(EnumSet.of(Category.SNEAKERS, Category.BOOTS), TargetParser.parse("footwear"));
        assertEquals(EnumSet.of(Category.BOOTS), TargetParser.parse(" BOOTS "));
    }

    @Test
    void unknownTargetIsRejected() {
        assertThrows(ControllerException.class, () -> TargetParser.parse("hats"));
    }
}
