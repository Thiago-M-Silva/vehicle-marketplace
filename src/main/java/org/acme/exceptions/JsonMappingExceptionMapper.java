package org.acme.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Exception mapper for handling JSON deserialization errors.
 * 
 * This class maps {@link InvalidFormatException} exceptions that occur during
 * JSON deserialization to HTTP 400 Bad Request responses. It is registered as
 * a JAX-RS provider and will automatically handle format conversion errors.
 * 
 * @author 
 * @version 1.0
 * @see ExceptionMapper
 * @see InvalidFormatException
 * @see Response
 */
@Provider
public class JsonMappingExceptionMapper implements ExceptionMapper<InvalidFormatException> {
    @Override
    public Response toResponse(InvalidFormatException e) {
        e.printStackTrace(); 

        return Response.status(Response.Status.BAD_REQUEST)
            .entity("Erro ao desserializar JSON: " + e.getMessage())
            .build();
    }
}
