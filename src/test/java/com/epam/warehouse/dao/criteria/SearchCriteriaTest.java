package com.epam.warehouse.dao.criteria;

import com.epam.warehouse.dao.criteria.parameter.BrandParameter;
import com.epam.warehouse.dao.criteria.parameter.HoodedParameter;
import com.epam.warehouse.dao.criteria.parameter.NameParameter;
import com.epam.warehouse.dao.criteria.parameter.Parameter;
import com.epam.warehouse.dao.criteria.parameter.PriceParameter;
import com.epam.warehouse.dao.criteria.parameter.ShoeSizeParameter;
import com.epam.warehouse.dao.criteria.parameter.SizeParameter;
import com.epam.warehouse.dao.criteria.parameter.WaterproofParameter;
import com.epam.warehouse.entity.Boots;
import com.epam.warehouse.entity.Jacket;
import com.epam.warehouse.entity.Jeans;
import com.epam.warehouse.entity.attribute.ClothingSize;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchCriteriaTest {

    private final Jacket parka = new Jacket().setId(1).setName("Storm Parka").setBrand("Columbia")
            .setPrice(new BigDecimal("149.99")).setSize(ClothingSize.L).setWaterproof(true).setHooded(true);

    @Test
    void emptyCriteriaMatchesEverything() {
        assertTrue(new JacketSearchCriteria().test(parka));
    }

    @Test
    void criteriaMatchesOnlyWhenAllParametersMatch() {
        SearchCriteria<Jacket> criteria = new JacketSearchCriteria()
                .add(new BrandParameter("columbia"))
                .add(new SizeParameter(Set.of(ClothingSize.L, ClothingSize.XL)))
                .add(new WaterproofParameter(true))
                .add(new HoodedParameter(true));
        assertTrue(criteria.test(parka));
        criteria.add(new PriceParameter(Range.atMost(new BigDecimal("100"))));
        assertFalse(criteria.test(parka));
    }

    @Test
    void anyAndNoneParameters() {
        assertTrue(new JacketSearchCriteria().add(Parameter.any()).test(parka));
        assertFalse(new JacketSearchCriteria().add(Parameter.none()).test(parka));
    }

    @Test
    void nullProductNeverMatches() {
        assertFalse(new JacketSearchCriteria().test(null));
    }

    @Test
    void addCheckedAcceptsParametersOfSuperTypes() {
        SearchCriteria<Boots> criteria = new BootsSearchCriteria()
                .addChecked(new NameParameter("boot"))
                .addChecked(new WaterproofParameter(true))
                .addChecked(new ShoeSizeParameter(Range.exactly(41.0)));
        assertEquals(3, criteria.parameters().size());
    }

    @Test
    void addCheckedRejectsInapplicableParameters() {
        SearchCriteria<Jeans> criteria = new JeansSearchCriteria();
        assertThrows(IllegalArgumentException.class, () -> criteria.addChecked(new WaterproofParameter(true)));
        assertThrows(IllegalArgumentException.class,
                () -> criteria.addChecked(new ShoeSizeParameter(Range.exactly(41.0))));
    }

    @Test
    void factoryCreatesCriteriaOfRequestedType() {
        SearchCriteria<Boots> criteria = CriteriaFactory.forType(Boots.class);
        assertInstanceOf(BootsSearchCriteria.class, criteria);
        assertEquals(Boots.class, criteria.productType());
        assertThrows(IllegalArgumentException.class, () -> CriteriaFactory.forType(String.class));
    }
}
