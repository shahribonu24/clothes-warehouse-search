package com.epam.warehouse.controller.parser;

import com.epam.warehouse.controller.ControllerException;
import com.epam.warehouse.service.Selection;
import com.epam.warehouse.service.SortDirection;
import com.epam.warehouse.service.SortField;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SelectionParserTest {

    @Test
    void defaultsWhenNothingGiven() throws ControllerException {
        assertEquals(Selection.DEFAULT, SelectionParser.parse(Map.of("brand", "Nike")));
    }

    @Test
    void sortOrderAndTop() throws ControllerException {
        assertEquals(new Selection(SortField.PRICE, SortDirection.DESC, 3, Selection.Edge.TOP),
                SelectionParser.parse(Map.of("sort", "price", "order", "DESC", "top", "3")));
    }

    @Test
    void bottom() throws ControllerException {
        assertEquals(new Selection(SortField.ID, SortDirection.ASC, 2, Selection.Edge.BOTTOM),
                SelectionParser.parse(Map.of("bottom", "2")));
    }

    @Test
    void wrongValuesAreRejected() {
        assertThrows(ControllerException.class, () -> SelectionParser.parse(Map.of("sort", "weight")));
        assertThrows(ControllerException.class, () -> SelectionParser.parse(Map.of("order", "up")));
        assertThrows(ControllerException.class, () -> SelectionParser.parse(Map.of("top", "0")));
        assertThrows(ControllerException.class, () -> SelectionParser.parse(Map.of("bottom", "x")));
        assertThrows(ControllerException.class, () -> SelectionParser.parse(Map.of("top", "1", "bottom", "1")));
    }
}
