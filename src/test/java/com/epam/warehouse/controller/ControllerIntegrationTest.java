package com.epam.warehouse.controller;

import com.epam.warehouse.TestData;
import com.epam.warehouse.controller.impl.ControllerImpl;
import com.epam.warehouse.dao.DaoFactory;
import com.epam.warehouse.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Controller + real service + DAO over the test CSV files (use cases from the report).
 */
class ControllerIntegrationTest {

    private static final Pattern ROW_ID = Pattern.compile("^(\\d+)\\s+\\|");

    private final Controller controller = new ControllerImpl(new ProductServiceImpl());

    @BeforeEach
    void setUp() {
        TestData.registerTestDaos();
    }

    @AfterEach
    void tearDown() {
        DaoFactory.clear();
    }

    /**
     * @return ids of the table rows in the order they are shown
     */
    private static List<Long> shownIds(Response response) {
        assertEquals(Response.Status.OK, response.status(), response.message());
        return Arrays.stream(response.message().split("\\R"))
                .map(ROW_ID::matcher)
                .filter(Matcher::find)
                .map(m -> Long.parseLong(m.group(1)))
                .toList();
    }

    @Test
    void listAll() {
        Response response = controller.execute("list");
        assertTrue(response.message().startsWith("Found 12 product(s):"));
        assertEquals(12, shownIds(response).size());
    }

    @Test
    void listCategorySorted() {
        assertEquals(List.of(4L, 1L, 2L, 3L), shownIds(controller.execute("list jackets sort=price order=desc")));
    }

    @Test
    void findByParametersInGroup() {
        assertEquals(List.of(1L, 4L), shownIds(controller.execute("find clothing size=L,M season=WINTER price=;300")));
    }

    @Test
    void findByTextWithSpaces() {
        Response response = controller.execute("find sneakers name=\"Air Max\"");
        assertEquals(List.of(201L), shownIds(response));
        assertTrue(response.message().contains("shoeSize=42.5, sport=LIFESTYLE"));
    }

    @Test
    void findTopAndBottom() {
        assertEquals(List.of(4L, 202L, 301L), shownIds(controller.execute("find all sort=price order=desc top=3")));
        assertEquals(List.of(3L, 102L), shownIds(controller.execute("find all sort=price bottom=2 order=desc")));
    }

    @Test
    void findWaterproofAcrossBranches() {
        assertEquals(List.of(1L, 3L, 4L, 302L), shownIds(controller.execute("find all waterproof=true")));
    }

    @Test
    void nothingFound() {
        Response response = controller.execute("find boots brand=Unknown");
        assertEquals(Response.Status.OK, response.status());
        assertEquals("No products found matching your criteria.", response.message());
    }

    @Test
    void inapplicableParameter() {
        Response response = controller.execute("find sneakers waterproof=true");
        assertEquals(Response.Status.ERROR, response.status());
        assertEquals("Error: Parameter 'waterproof' is not applicable to sneakers", response.message());
    }

    @ParameterizedTest
    @ValueSource(strings = {"fnd", "find jackets price=abc", "find jackets size=M size=L", "find hats",
            "find jackets weight=5", "list all sort=weight", "help fly", ""})
    void wrongInputGivesErrorAndProgramContinues(String line) {
        Response response = controller.execute(line);
        assertEquals(Response.Status.ERROR, response.status());
        assertTrue(response.message().startsWith("Error: "));
        // the controller keeps working after an error
        assertEquals(Response.Status.OK, controller.execute("list boots").status());
    }

    @Test
    void stats() {
        Response response = controller.execute("stats footwear");
        String message = response.message();
        assertTrue(message.startsWith("Inventory statistics:"));
        assertTrue(message.contains("sneakers | 3     | 12    | 1            | 1759.88"), message);
        assertTrue(message.contains("boots    | 2     | 9     | 0            | 1372.50"), message);
        assertTrue(message.contains("TOTAL    | 5     | 21    | 1            | 3132.38"), message);
    }

    @Test
    void cost() {
        assertEquals("Total cost: 1594.97 (2 product line(s), 8 unit(s))",
                controller.execute("cost clothing season=WINTER").message());
        assertEquals("Total cost: 7051.08 (12 product line(s), 60 unit(s))", controller.execute("cost").message());
        assertEquals("No products found matching your criteria. Total cost: 0.00",
                controller.execute("cost all brand=Unknown").message());
    }

    @Test
    void help() {
        Response general = controller.execute("help");
        assertTrue(general.message().contains("find <target>"));
        assertTrue(general.message().contains("cost [target]"));

        Response find = controller.execute("help find");
        assertTrue(find.message().contains("waterproof | true or false"), find.message());
        assertTrue(find.message().contains("jackets, boots"));
        assertTrue(find.message().contains("Sort fields: id, name, brand, category, price, quantity, value"));
    }

    @Test
    void exit() {
        Response response = controller.execute("EXIT");
        assertEquals(Response.Status.EXIT, response.status());
        assertEquals("Goodbye!", response.message());
    }
}
