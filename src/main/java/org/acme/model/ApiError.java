package org.acme.model;

import java.time.Instant;

/**
 * Represents an API error response containing details about an error that occurred
 * during API request processing.
 *
 * @author Generated
 * @version 1.0
 */
public class ApiError {
    /**
     * The timestamp when the error occurred. Automatically set to the current instant
     * upon object creation.
     */
    public Instant timestamp = Instant.now();

    /**
     * The HTTP status code associated with the error (e.g., 400, 404, 500).
     */
    public int status;

    /**
     * A brief error type or category description (e.g., "Bad Request", "Not Found").
     */
    public String error;

    /**
     * An application-specific error code that uniquely identifies the error type.
     */
    public String code;

    /**
     * A detailed human-readable message describing the error.
     */
    public String message;

    /**
     * The API endpoint path where the error occurred.
     */
    public String path;

    /**
     * Constructs an ApiError with the specified details.
     *
     * @param status the HTTP status code
     * @param error the error type or category
     * @param code the application-specific error code
     * @param message the detailed error message
     * @param path the API endpoint path where the error occurred
     */
    public ApiError(int status, String error, String code, String message, String path) {
        this.status = status;
        this.error = error;
        this.code = code;
        this.message = message;
        this.path = path;
    }
}
