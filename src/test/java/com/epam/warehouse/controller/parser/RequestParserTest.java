package com.epam.warehouse.controller.parser;

import com.epam.warehouse.controller.ControllerException;
import com.epam.warehouse.controller.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestParserTest {

    private final RequestParser parser = new RequestParser();

    @Test
    void parsesCommandArgumentsAndOptions() throws ControllerException {
        Request request = parser.parse("  FIND  Jackets size=M,L  Price=;150 ");
        assertEquals("find", request.command());
        assertEquals(List.of("Jackets"), request.arguments());
        assertEquals(Map.of("size", "M,L", "price", ";150"), request.options());
    }

    @Test
    void keepsOptionOrder() throws ControllerException {
        Request request = parser.parse("find all sort=price brand=Nike order=desc");
        assertEquals(List.of("sort", "brand", "order"), List.copyOf(request.options().keySet()));
    }

    @Test
    void commandWithoutArguments() throws ControllerException {
        Request request = parser.parse("exit");
        assertEquals("exit", request.command());
        assertTrue(request.arguments().isEmpty());
        assertTrue(request.options().isEmpty());
        assertTrue(request.argument(0).isEmpty());
    }

    @Test
    void quotedValueMayContainSpaces() throws ControllerException {
        Request request = parser.parse("find sneakers name=\"Air  Max\" brand=\"The North Face\"");
        assertEquals("Air  Max", request.options().get("name"));
        assertEquals("The North Face", request.options().get("brand"));
    }

    @Test
    void quotedArgument() throws ControllerException {
        assertEquals(List.of("ab c"), parser.parse("help \"ab c\"").arguments());
    }

    @Test
    void tokenize() throws ControllerException {
        assertEquals(List.of("a", "b=c d", "e"), RequestParser.tokenize("a  b=\"c d\"\te"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void emptyRequestIsRejected(String line) {
        assertThrows(ControllerException.class, () -> parser.parse(line));
    }

    @Test
    void duplicateParameterIsRejected() {
        ControllerException e = assertThrows(ControllerException.class,
                () -> parser.parse("find jackets size=M SIZE=L"));
        assertTrue(e.getMessage().contains("more than once"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "find jackets price=",          // no value
            "find jackets =10",             // no name
            "find jackets name=\"Parka",    // no closing quote
            "size=M",                       // no command
            "find size=M jackets"           // argument after options
    })
    void malformedRequestIsRejected(String line) {
        assertThrows(ControllerException.class, () -> parser.parse(line));
    }
}
