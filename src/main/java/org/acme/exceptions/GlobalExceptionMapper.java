package org.acme.exceptions;

import org.acme.model.ApiError;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Global exception mapper to handle various exceptions and return structured JSON responses.
 */
@Provider
@ApplicationScoped
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable e) {
        LOG.error("Exception caught", e);

        if (e instanceof InvalidFormatException ife) {
            return build(Response.Status.BAD_REQUEST, "INVALID_JSON_FORMAT", 
                "Invalid value for field '" + extractFieldName(ife) + "'", ife.getOriginalMessage());
        }

        if (e instanceof JsonProcessingException jpe) {
            return build(Response.Status.BAD_REQUEST, "INVALID_JSON_SYNTAX", "Malformed JSON body", jpe.getOriginalMessage());
        }

        if (e instanceof org.hibernate.exception.ConstraintViolationException db) {
            return build(Response.Status.CONFLICT, "DATABASE_CONSTRAINT", "Constraint violation", db.getConstraintName());
        }

        if (e instanceof IllegalArgumentException iae) {
            return build(Response.Status.BAD_REQUEST, "ILLEGAL_ARGUMENT", iae.getMessage(), null);
        }

        /*
        you can add more custom exceptions here
            if (e instanceof UserNotFoundException unf) {
                return build(Response.Status.NOT_FOUND, "USER_NOT_FOUND", unf.getMessage(), null);
            }
         */

        return build(Response.Status.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", 
                "An unexpected error occurred", e.getMessage());
    }

    private Response build(Response.Status status, String code, String message, String details) {
        var error = new ApiError(
                status.getStatusCode(),
                status.getReasonPhrase(),
                code,
                message + (details != null ? ": " + details : ""),
                uriInfo != null ? uriInfo.getPath() : "unknown"
        );

        return Response.status(status)
                .entity(error)
                .build();
    }

    private String extractFieldName(InvalidFormatException e) {
        if (e.getPath() != null && !e.getPath().isEmpty()) {
            return e.getPath().get(0).getFieldName();
        }
        return "unknown";
    }
    
}
