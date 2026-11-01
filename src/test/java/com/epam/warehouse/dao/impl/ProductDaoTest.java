package com.epam.warehouse.dao.impl;

import com.epam.warehouse.TestData;
import com.epam.warehouse.dao.DaoException;
import com.epam.warehouse.dao.DaoFactory;
import com.epam.warehouse.dao.ProductDao;
import com.epam.warehouse.dao.criteria.JacketSearchCriteria;
import com.epam.warehouse.dao.criteria.JeansSearchCriteria;
import com.epam.warehouse.dao.criteria.Range;
import com.epam.warehouse.dao.criteria.SneakersSearchCriteria;
import com.epam.warehouse.dao.criteria.parameter.BrandParameter;
import com.epam.warehouse.dao.criteria.parameter.ColorParameter;
import com.epam.warehouse.dao.criteria.parameter.FitParameter;
import com.epam.warehouse.dao.criteria.parameter.Parameter;
import com.epam.warehouse.dao.criteria.parameter.PriceParameter;
import com.epam.warehouse.dao.criteria.parameter.QuantityParameter;
import com.epam.warehouse.dao.criteria.parameter.SeasonParameter;
import com.epam.warehouse.dao.criteria.parameter.SportTypeParameter;
import com.epam.warehouse.dao.criteria.parameter.WaistParameter;
import com.epam.warehouse.dao.criteria.parameter.WaterproofParameter;
import com.epam.warehouse.dao.mapper.JacketMapper;
import com.epam.warehouse.dao.source.CsvSource;
import com.epam.warehouse.entity.Boots;
import com.epam.warehouse.entity.Jacket;
import com.epam.warehouse.entity.Jeans;
import com.epam.warehouse.entity.Product;
import com.epam.warehouse.entity.Sneakers;
import com.epam.warehouse.entity.attribute.Fit;
import com.epam.warehouse.entity.attribute.Season;
import com.epam.warehouse.entity.attribute.SportType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductDaoTest {

    @BeforeEach
    void setUp() {
        TestData.registerTestDaos();
    }

    private static List<Long> ids(List<? extends Product<?>> products) {
        return products.stream().map(Product::getId).toList();
    }

    @Test
    void findWithAnyParameterReturnsWholeFile() throws DaoException {
        ProductDao<Jacket> dao = DaoFactory.getInstance(Jacket.class);
        assertEquals(TestData.JACKETS_COUNT, dao.find(new JacketSearchCriteria().add(Parameter.any())).size());
    }

    @Test
    void findByCommonParameters() throws DaoException {
        ProductDao<Jacket> dao = DaoFactory.getInstance(Jacket.class);
        List<Jacket> result = dao.find(new JacketSearchCriteria()
                .add(new ColorParameter("black"))
                .add(new SeasonParameter(Set.of(Season.WINTER)))
                .add(new PriceParameter(Range.atMost(new BigDecimal("200")))));
        assertEquals(List.of(1L), ids(result));
    }

    @Test
    void findByInterfaceParameter() throws DaoException {
        List<Jacket> result = DaoFactory.getInstance(Jacket.class)
                .find(new JacketSearchCriteria().add(new WaterproofParameter(true)));
        assertEquals(List.of(1L, 3L, 4L), ids(result));
    }

    @Test
    void findByTypeSpecificParameters() throws DaoException {
        List<Jeans> jeans = DaoFactory.getInstance(Jeans.class).find(new JeansSearchCriteria()
                .add(new WaistParameter(new Range<>(30, 40)))
                .add(new FitParameter(Set.of(Fit.STRAIGHT, Fit.RELAXED))));
        assertEquals(List.of(101L, 103L), ids(jeans));

        List<Sneakers> sneakers = DaoFactory.getInstance(Sneakers.class).find(new SneakersSearchCriteria()
                .add(new SportTypeParameter(Set.of(SportType.RUNNING))));
        assertEquals(List.of(202L), ids(sneakers));
    }

    @Test
    void findReturnsEmptyListWhenNothingMatches() throws DaoException {
        List<Jacket> result = DaoFactory.getInstance(Jacket.class)
                .find(new JacketSearchCriteria().add(new BrandParameter("Gucci")));
        assertTrue(result.isEmpty());
    }

    @Test
    void outOfStockProductsCanBeFiltered() throws DaoException {
        List<Jacket> result = DaoFactory.getInstance(Jacket.class)
                .find(new JacketSearchCriteria().add(new QuantityParameter(Range.atLeast(1))));
        assertEquals(List.of(1L, 2L, 4L), ids(result));
    }

    @Test
    void repeatedSearchesReadTheSourceAgain() throws DaoException {
        ProductDao<Jacket> dao = DaoFactory.getInstance(Jacket.class);
        assertEquals(dao.find(new JacketSearchCriteria()), dao.find(new JacketSearchCriteria()));
    }

    @Test
    void corruptedSourceCausesDaoException() {
        ProductDao<Jacket> dao = new ProductDaoImpl<>(
                new CsvSource<>("test-data/jackets-invalid-price.csv", new JacketMapper()));
        assertThrows(DaoException.class, () -> dao.find(new JacketSearchCriteria()));
    }

    @Test
    void factoryThrowsForUnregisteredType() {
        DaoFactory.clear();
        assertThrows(DaoException.class, () -> DaoFactory.getInstance(Boots.class));
    }
}
