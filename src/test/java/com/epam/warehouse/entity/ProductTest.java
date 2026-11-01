package com.epam.warehouse.entity;

import com.epam.warehouse.entity.attribute.ClothingSize;
import com.epam.warehouse.entity.attribute.Fit;
import com.epam.warehouse.entity.attribute.Gender;
import com.epam.warehouse.entity.attribute.Season;
import com.epam.warehouse.entity.attribute.SportType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductTest {

    private static Jacket jacket() {
        return new Jacket()
                .setId(1).setName("Storm Parka").setBrand("Columbia").setColor("Black")
                .setGender(Gender.MEN).setMaterial("Polyester").setPrice(new BigDecimal("149.99"))
                .setQuantity(3).setSize(ClothingSize.L).setSeason(Season.WINTER)
                .setWaterproof(true).setHooded(true);
    }

    @Test
    void fluentSettersReturnConcreteTypeAndStoreValues() {
        Jacket jacket = jacket();
        assertAll(
                () -> assertEquals(1, jacket.getId()),
                () -> assertEquals("Storm Parka", jacket.getName()),
                () -> assertEquals(ClothingSize.L, jacket.getSize()),
                () -> assertEquals(Season.WINTER, jacket.getSeason()),
                () -> assertTrue(jacket.isWaterproof()),
                () -> assertTrue(jacket.isHooded()),
                () -> assertEquals(Category.JACKETS, jacket.getCategory())
        );
    }

    @Test
    void stockValueIsPriceMultipliedByQuantity() {
        assertEquals(new BigDecimal("449.97"), jacket().getStockValue());
    }

    @Test
    void priceIsNormalizedToTwoDecimals() {
        assertEquals(new BigDecimal("10.00"), jacket().setPrice(BigDecimal.TEN).getPrice());
    }

    @Test
    void productWithZeroQuantityIsNotInStock() {
        assertFalse(jacket().setQuantity(0).isInStock());
        assertTrue(jacket().isInStock());
    }

    @Test
    void equalProductsHaveEqualHashCodes() {
        assertEquals(jacket(), jacket());
        assertEquals(jacket().hashCode(), jacket().hashCode());
    }

    @Test
    void productsWithDifferentSpecificFieldsAreNotEqual() {
        assertNotEquals(jacket(), jacket().setHooded(false));
        assertNotEquals(jacket(), jacket().setSize(ClothingSize.M));
    }

    @Test
    void productsOfDifferentTypesAreNotEqual() {
        Boots boots = new Boots().setId(1);
        Sneakers sneakers = new Sneakers().setId(1);
        assertNotEquals(boots, sneakers);
    }

    @Test
    void toStringContainsCommonAndSpecificAttributes() {
        String text = jacket().toString();
        assertTrue(text.startsWith("Jacket{"));
        assertTrue(text.contains("name='Storm Parka'"));
        assertTrue(text.contains("stockValue=449.97"));
        assertTrue(text.contains("size=L"));
        assertTrue(text.contains("hooded=true"));
    }

    @Test
    void specificAttributesFollowHierarchyOrder() {
        assertEquals(List.of("size", "season", "waterproof", "hooded"),
                List.copyOf(jacket().specificAttributes().keySet()));
        Jeans jeans = new Jeans().setWaist(32).setLength(34).setFit(Fit.SLIM)
                .setSize(ClothingSize.M).setSeason(Season.ALL_SEASON);
        assertEquals(List.of("size", "season", "waist", "length", "fit"),
                List.copyOf(jeans.specificAttributes().keySet()));
        Sneakers sneakers = new Sneakers().setShoeSize(42.5).setSportType(SportType.RUNNING);
        assertEquals(List.of("shoeSize", "sport"), List.copyOf(sneakers.specificAttributes().keySet()));
    }

    @Test
    void invalidValuesAreRejected() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new Jacket().setId(0)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Jacket().setName(" ")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Jacket().setPrice(new BigDecimal("-1"))),
                () -> assertThrows(IllegalArgumentException.class, () -> new Jacket().setQuantity(-5)),
                () -> assertThrows(NullPointerException.class, () -> new Jacket().setGender(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Jeans().setWaist(10)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Boots().setShaftHeight(0))
        );
    }

    @ParameterizedTest
    @ValueSource(doubles = {15.5, 50.5, 42.3, -1})
    void invalidShoeSizesAreRejected(double size) {
        assertThrows(IllegalArgumentException.class, () -> new Sneakers().setShoeSize(size));
    }

    @ParameterizedTest
    @ValueSource(doubles = {16, 38, 42.5, 50})
    void validShoeSizesAreAccepted(double size) {
        assertEquals(size, new Boots().setShoeSize(size).getShoeSize());
    }

    @Test
    void productIsSerializable() throws IOException, ClassNotFoundException {
        Jacket original = jacket();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(original);
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            assertEquals(original, in.readObject());
        }
    }
}
