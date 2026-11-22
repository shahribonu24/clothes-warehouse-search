package com.epam.warehouse.controller;

import java.util.Objects;

/**
 * Result of a request, ready to be shown by the view.
 *
 * @param status  {@code OK} — request executed; {@code ERROR} — bad input or data failure;
 *                {@code EXIT} — the user asked to finish the program
 * @param message text to show to the user
 */
public record Response(Status status, String message) {

    public enum Status {
        OK, ERROR, EXIT
    }

    public Response {
        Objects.requireNonNull(status, "Status must not be null");
        Objects.requireNonNull(message, "Message must not be null");
    }

    public static Response ok(String message) {
        return new Response(Status.OK, message);
    }

    public static Response error(String message) {
        return new Response(Status.ERROR, "Error: " + message);
    }

    public static Response exit(String message) {
        return new Response(Status.EXIT, message);
    }

    public boolean isError() {
        return status == Status.ERROR;
    }
}
