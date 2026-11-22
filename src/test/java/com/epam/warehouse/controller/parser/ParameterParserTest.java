package com.epam.warehouse.controller.parser;

import com.epam.warehouse.controller.ControllerException;
import com.epam.warehouse.dao.criteria.Range;
import com.epam.warehouse.dao.criteria.parameter.Parameter;
import com.epam.warehouse.dao.criteria.parameter.PriceParameter;
import com.epam.warehouse.dao.criteria.parameter.SizeParameter;
import com.epam.warehouse.dao.criteria.parameter.WaterproofParameter;
import com.epam.warehouse.entity.attribute.ClothingSize;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParameterParserTest {

    @Test
    void createsParametersInOrderAndSkipsIgnoredKeys() throws ControllerException {
        Map<String, String> options = new LinkedHashMap<>();
        options.put("size", "M,L");
        options.put("sort", "price");
        options.put("price", ";150");
        options.put("waterproof", "yes");
        List<Parameter<?>> parameters = ParameterParser.parse(options, SelectionParser.KEYS);
        assertEquals(List.of(
                new SizeParameter(Set.of(ClothingSize.M, ClothingSize.L)),
                new PriceParameter(Range.atMost(new BigDecimal("150"))),
                new WaterproofParameter(true)), parameters);
    }

    @Test
    void unknownParameterIsRejected() {
        ControllerException e = assertThrows(ControllerException.class,
                () -> ParameterParser.parse(Map.of("weight", "5"), Set.of()));
        assertTrue(e.getMessage().contains("weight"));
    }

    @Test
    void wrongValueIsRejectedWithParameterName() {
        ControllerException e = assertThrows(ControllerException.class,
                () -> ParameterParser.parse(Map.of("price", "abc"), Set.of()));
        assertTrue(e.getMessage().startsWith("Wrong value of 'price'"));
        assertThrows(ControllerException.class, () -> ParameterParser.parse(Map.of("price", "-5;"), Set.of()));
        assertThrows(ControllerException.class, () -> ParameterParser.parse(Map.of("season", "spring"), Set.of()));
    }

    @Test
    void keysAreUniqueAndApplicableCategoriesAreKnown() {
        assertEquals(ParameterType.values().length,
                Set.of(ParameterType.values()).stream().map(ParameterType::key).distinct().count());
        assertEquals("jackets, boots", ParameterType.WATERPROOF.applicableCategories());
        assertEquals("jeans", ParameterType.FIT.applicableCategories());
        assertEquals(ParameterType.SHOE_SIZE, ParameterType.byKey("ShoeSize").orElseThrow());
    }

    @ParameterizedTest
    @EnumSource(ParameterType.class)
    void everyParameterTypeCreatesParameterWithItsOwnName(ParameterType type) {
        String sample = switch (type) {
            case GENDER -> "MEN";
            case SIZE -> "M";
            case SEASON -> "WINTER";
            case FIT -> "SLIM";
            case SPORT -> "RUNNING";
            case WATERPROOF, HOODED -> "true";
            case NAME, BRAND, COLOR, MATERIAL -> "text";
            default -> "30";
        };
        Parameter<?> parameter = type.create(sample);
        assertEquals(type.key(), parameter.name());
        assertFalse(type.applicableCategories().isEmpty());
    }
}
