package org.acme.exceptions;

import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

//TODO: Impement and style a proper error response body and logging mechanism
@Provider
public class JsonMappingExceptionMapper implements ExceptionMapper<InvalidFormatException> {
    @Override
    public Response toResponse(InvalidFormatException e) {
        e.printStackTrace(); // log detalhado
        return Response.status(Response.Status.BAD_REQUEST)
            .entity("Erro ao desserializar JSON: " + e.getMessage())
            .build();
    }
}
