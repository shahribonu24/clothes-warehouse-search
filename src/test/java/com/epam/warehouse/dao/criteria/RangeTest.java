package com.epam.warehouse.dao.criteria;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RangeTest {

    @Test
    void closedRangeIncludesBorders() {
        Range<Integer> range = new Range<>(10, 20);
        assertTrue(range.contains(10));
        assertTrue(range.contains(15));
        assertTrue(range.contains(20));
        assertFalse(range.contains(9));
        assertFalse(range.contains(21));
    }

    @Test
    void openRangesHaveOnlyOneBorder() {
        assertTrue(Range.atLeast(5).contains(1_000_000));
        assertFalse(Range.atLeast(5).contains(4));
        assertTrue(Range.atMost(5).contains(-3));
        assertFalse(Range.atMost(5).contains(6));
    }

    @Test
    void exactRangeContainsOnlyItsValue() {
        assertTrue(Range.exactly(7L).contains(7L));
        assertFalse(Range.exactly(7L).contains(8L));
    }

    @Test
    void invalidRangesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Range<>(20, 10));
        assertThrows(IllegalArgumentException.class, () -> new Range<Integer>(null, null));
    }

    @Test
    void toStringUsesRequestFormat() {
        assertEquals("10;20", new Range<>(10, 20).toString());
        assertEquals(";20", Range.atMost(20).toString());
        assertEquals("10;", Range.atLeast(10).toString());
        assertEquals("7", Range.exactly(7).toString());
    }
}
