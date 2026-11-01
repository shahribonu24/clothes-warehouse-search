package com.epam.warehouse.dao.criteria;

import com.epam.warehouse.dao.criteria.parameter.BrandParameter;
import com.epam.warehouse.dao.criteria.parameter.GenderParameter;
import com.epam.warehouse.dao.criteria.parameter.IdParameter;
import com.epam.warehouse.dao.criteria.parameter.MaterialParameter;
import com.epam.warehouse.dao.criteria.parameter.NameParameter;
import com.epam.warehouse.dao.criteria.parameter.PriceParameter;
import com.epam.warehouse.dao.criteria.parameter.QuantityParameter;
import com.epam.warehouse.dao.criteria.parameter.SizeParameter;
import com.epam.warehouse.dao.criteria.parameter.WaterproofParameter;
import com.epam.warehouse.entity.Boots;
import com.epam.warehouse.entity.Jacket;
import com.epam.warehouse.entity.Jeans;
import com.epam.warehouse.entity.Product;
import com.epam.warehouse.entity.Sneakers;
import com.epam.warehouse.entity.attribute.ClothingSize;
import com.epam.warehouse.entity.attribute.Gender;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParameterTest {

    private final Boots boots = new Boots().setId(5).setName("Chelsea Boot").setBrand("Dr. Martens")
            .setMaterial("Smooth leather").setGender(Gender.UNISEX).setPrice(new BigDecimal("169"))
            .setQuantity(6).setWaterproof(false);

    @Test
    void textParametersAreCaseInsensitive() {
        assertTrue(new NameParameter("CHELSEA").test(boots));
        assertTrue(new MaterialParameter("leather").test(boots));
        assertTrue(new BrandParameter("dr. martens").test(boots));
        assertFalse(new BrandParameter("Dr.").test(boots));
    }

    @Test
    void rangeParameters() {
        assertTrue(new PriceParameter(new Range<>(new BigDecimal("100"), new BigDecimal("169"))).test(boots));
        assertFalse(new PriceParameter(Range.atMost(new BigDecimal("168.99"))).test(boots));
        assertTrue(new QuantityParameter(Range.atLeast(1)).test(boots));
        assertTrue(new IdParameter(Range.exactly(5L)).test(boots));
    }

    @Test
    void valueSetParameters() {
        assertTrue(new GenderParameter(List.of(Gender.MEN, Gender.UNISEX)).test(boots));
        assertFalse(new GenderParameter(List.of(Gender.WOMEN)).test(boots));
        assertTrue(new WaterproofParameter(false).test(boots));
    }

    @Test
    void attributeThatIsNotSetDoesNotMatch() {
        assertFalse(new NameParameter("x").test(new Jacket()));
    }

    @Test
    void invalidParameterValuesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new PriceParameter(Range.atLeast(new BigDecimal("-1"))));
        assertThrows(IllegalArgumentException.class, () -> new QuantityParameter(Range.atMost(-1)));
        assertThrows(IllegalArgumentException.class, () -> new IdParameter(Range.exactly(0L)));
        assertThrows(IllegalArgumentException.class, () -> new NameParameter(" "));
        assertThrows(IllegalArgumentException.class, () -> new SizeParameter(Set.of()));
    }

    @Test
    void applicabilityFollowsTheClassHierarchy() {
        SizeParameter size = new SizeParameter(Set.of(ClothingSize.M));
        assertTrue(size.isApplicableTo(Jacket.class));
        assertTrue(size.isApplicableTo(Jeans.class));
        assertFalse(size.isApplicableTo(Sneakers.class));

        WaterproofParameter waterproof = new WaterproofParameter(true);
        assertTrue(waterproof.isApplicableTo(Jacket.class));
        assertTrue(waterproof.isApplicableTo(Boots.class));
        assertFalse(waterproof.isApplicableTo(Sneakers.class));

        assertTrue(new NameParameter("x").isApplicableTo(Product.class));
    }

    @Test
    void equalityAndStringRepresentation() {
        assertEquals(new BrandParameter("Nike"), new BrandParameter("NIKE"));
        assertNotEquals(new BrandParameter("Nike"), new NameParameter("Nike"));
        assertEquals("price=10;20", new PriceParameter(new Range<>(BigDecimal.TEN, new BigDecimal("20"))).toString());
    }
}
