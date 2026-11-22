package com.epam.warehouse.service;

import com.epam.warehouse.TestData;
import com.epam.warehouse.dao.DaoFactory;
import com.epam.warehouse.dao.criteria.Range;
import com.epam.warehouse.dao.criteria.parameter.BrandParameter;
import com.epam.warehouse.dao.criteria.parameter.HoodedParameter;
import com.epam.warehouse.dao.criteria.parameter.Parameter;
import com.epam.warehouse.dao.criteria.parameter.PriceParameter;
import com.epam.warehouse.dao.criteria.parameter.SeasonParameter;
import com.epam.warehouse.dao.criteria.parameter.SizeParameter;
import com.epam.warehouse.dao.criteria.parameter.WaterproofParameter;
import com.epam.warehouse.dao.impl.ProductDaoImpl;
import com.epam.warehouse.dao.mapper.JacketMapper;
import com.epam.warehouse.dao.source.CsvSource;
import com.epam.warehouse.entity.Category;
import com.epam.warehouse.entity.Clothing;
import com.epam.warehouse.entity.Footwear;
import com.epam.warehouse.entity.Jacket;
import com.epam.warehouse.entity.Product;
import com.epam.warehouse.entity.attribute.ClothingSize;
import com.epam.warehouse.entity.attribute.Season;
import com.epam.warehouse.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductServiceImplTest {

    private static final Set<Category> ALL = EnumSet.allOf(Category.class);

    private final ProductService service = new ProductServiceImpl();

    @BeforeEach
    void setUp() {
        TestData.registerTestDaos();
    }

    @AfterEach
    void tearDown() {
        DaoFactory.clear();
    }

    private static List<Long> ids(List<Product<?>> products) {
        return products.stream().map(Product::getId).toList();
    }

    @Test
    void factoryReturnsSameServiceInstance() {
        assertTrue(ServiceFactory.getInstance().getProductService() instanceof ProductServiceImpl);
        assertEquals(ServiceFactory.getInstance().getProductService(), ServiceFactory.getInstance().getProductService());
    }

    @Test
    void findAllReturnsEveryCategorySortedById() throws ServiceException {
        List<Product<?>> products = service.findAll(ALL, Selection.DEFAULT);
        assertEquals(TestData.TOTAL_COUNT, products.size());
        assertEquals(List.of(1L, 2L, 3L, 4L, 101L, 102L, 103L, 201L, 202L, 203L, 301L, 302L), ids(products));
    }

    @Test
    void findInOneCategory() throws ServiceException {
        List<Product<?>> products = service.find(EnumSet.of(Category.JACKETS),
                List.of(new SizeParameter(Set.of(ClothingSize.M))), Selection.DEFAULT);
        assertEquals(List.of(2L, 4L), ids(products));
    }

    @Test
    void findWithCommonParameterInSeveralCategories() throws ServiceException {
        List<Product<?>> products = service.find(Category.ofKind(Product.class),
                List.of(new PriceParameter(new Range<>(new BigDecimal("100"), new BigDecimal("150")))),
                Selection.DEFAULT);
        assertEquals(List.of(1L, 201L, 203L, 302L), ids(products));
    }

    @Test
    void parameterOfOtherBranchSkipsCategoriesWhereItIsNotApplicable() throws ServiceException {
        // waterproof applies to jackets and boots, but not to jeans and sneakers
        List<Product<?>> products = service.find(ALL, List.of(new WaterproofParameter(true)), Selection.DEFAULT);
        assertEquals(List.of(1L, 3L, 4L, 302L), ids(products));
    }

    @Test
    void parameterNotApplicableToAnyCategoryIsRejected() {
        ServiceException e = assertThrows(ServiceException.class, () -> service.find(
                Category.ofKind(Footwear.class), List.of(new HoodedParameter(true)), Selection.DEFAULT));
        assertTrue(e.getMessage().contains("hooded"));
        assertTrue(e.getMessage().contains("boots, sneakers"));
    }

    @Test
    void nothingFoundGivesEmptyList() throws ServiceException {
        assertTrue(service.find(ALL, List.of(new BrandParameter("Unknown")), Selection.DEFAULT).isEmpty());
    }

    @Test
    void emptyCategoriesAreRejected() {
        assertThrows(ServiceException.class, () -> service.findAll(Set.of(), Selection.DEFAULT));
        assertThrows(ServiceException.class, () -> service.findAll(null, Selection.DEFAULT));
    }

    @Test
    void nullParameterIsRejected() {
        List<Parameter<?>> parameters = new java.util.ArrayList<>();
        parameters.add(null);
        assertThrows(ServiceException.class, () -> service.find(ALL, parameters, Selection.DEFAULT));
    }

    @Test
    void topThreeMostExpensive() throws ServiceException {
        Selection selection = Selection.sortedBy(SortField.PRICE, SortDirection.DESC).withLimit(3, Selection.Edge.TOP);
        assertEquals(List.of(4L, 202L, 301L), ids(service.findAll(ALL, selection)));
    }

    @Test
    void bottomTwoOfAscendingOrderAreTheMostExpensive() throws ServiceException {
        Selection selection = Selection.sortedBy(SortField.PRICE, SortDirection.ASC)
                .withLimit(2, Selection.Edge.BOTTOM);
        assertEquals(List.of(202L, 4L), ids(service.findAll(ALL, selection)));
    }

    @Test
    void sortByCategoryThenId() throws ServiceException {
        Selection selection = Selection.sortedBy(SortField.CATEGORY, SortDirection.DESC);
        assertEquals(List.of(301L, 302L, 201L, 202L, 203L, 101L, 102L, 103L, 1L, 2L, 3L, 4L),
                ids(service.findAll(ALL, selection)));
    }

    @Test
    void statisticsOfWholeInventory() throws ServiceException {
        InventoryStatistics statistics = service.statistics(ALL, List.of());
        assertEquals(4, statistics.categories().size());
        assertEquals(TestData.TOTAL_COUNT, statistics.totalLines());
        assertEquals(60, statistics.totalUnits());
        assertEquals(new BigDecimal("7051.08"), statistics.totalValue());
        assertEquals(2, statistics.totalOutOfStock());

        CategoryStatistics jackets = statistics.categories().get(0);
        assertEquals(new CategoryStatistics(Category.JACKETS, 4, 18, new BigDecimal("2489.97"), 1), jackets);
    }

    @Test
    void statisticsShowZerosForCategoriesWhereParameterIsNotApplicable() throws ServiceException {
        InventoryStatistics statistics = service.statistics(Category.ofKind(Clothing.class),
                List.of(new HoodedParameter(true)));
        assertEquals(List.of(Category.JACKETS, Category.JEANS),
                statistics.categories().stream().map(CategoryStatistics::category).toList());
        assertEquals(new CategoryStatistics(Category.JEANS, 0, 0, new BigDecimal("0.00"), 0),
                statistics.categories().get(1));
        assertEquals(3, statistics.totalLines());
    }

    @Test
    void costOfMatchingProducts() throws ServiceException {
        BigDecimal cost = service.cost(Category.ofKind(Clothing.class),
                List.of(new SeasonParameter(Set.of(Season.WINTER))));
        // 149.99 x 3 + 229.00 x 5
        assertEquals(new BigDecimal("1594.97"), cost);
    }

    @Test
    void costOfNothingIsZero() throws ServiceException {
        assertEquals(0, BigDecimal.ZERO.compareTo(service.cost(ALL, List.of(new BrandParameter("Unknown")))));
    }

    @Test
    void daoFailureIsWrappedIntoServiceException() {
        DaoFactory.register(Jacket.class,
                new ProductDaoImpl<>(new CsvSource<>("test-data/jackets-invalid-price.csv", new JacketMapper())));
        ServiceException e = assertThrows(ServiceException.class,
                () -> service.findAll(EnumSet.of(Category.JACKETS), Selection.DEFAULT));
        assertTrue(e.getMessage().contains("line 3"));
    }

    @Test
    void missingDaoIsReportedAsServiceException() {
        DaoFactory.clear();
        assertThrows(ServiceException.class, () -> service.findAll(EnumSet.of(Category.BOOTS), Selection.DEFAULT));
    }
}
