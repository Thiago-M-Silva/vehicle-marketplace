package org.acme.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;

import jakarta.ws.rs.core.UriInfo;

@org.junit.jupiter.api.extension.ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class GlobalExceptionMapperTest {

    @InjectMocks
    private GlobalExceptionMapper mapper;

    @Mock
    private UriInfo uriInfo;

    @BeforeEach
    void setUp() {
        when(uriInfo.getPath()).thenReturn("/api/vehicles");
    }

    // TODO: corrigir esses testes
    // @Test
    // void testInvalidFormatException() {
    //     InvalidFormatException e = mock(InvalidFormatException.class);
    //     when(e.getPath()).thenReturn(java.util.List.of(mock(Reference.class)));
    //     when(e.getPath().get(0).getFieldName()).thenReturn("price");
    //     when(e.getOriginalMessage()).thenReturn("not a valid number");

    //     Response response = mapper.toResponse(e);

    //     assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    //     ApiError error = (ApiError) response.getEntity();
    //     assertEquals("INVALID_JSON_FORMAT", error);
    // }

    // @Test
    // void testJsonProcessingException() {
    //     JsonProcessingException e = mock(JsonProcessingException.class);
    //     when(e.getOriginalMessage()).thenReturn("unexpected end of input");

    //     Response response = mapper.toResponse(e);

    //     assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    //     ApiError error = (ApiError) response.getEntity();
    //     assertEquals("INVALID_JSON_SYNTAX", error);
    // }

    // @Test
    // void testConstraintViolationException() {
    //     org.hibernate.exception.ConstraintViolationException e = mock(org.hibernate.exception.ConstraintViolationException.class);
    //     when(e.getConstraintName()).thenReturn("uk_vehicle_vin");

    //     Response response = mapper.toResponse(e);

    //     assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
    //     ApiError error = (ApiError) response.getEntity();
    //     assertEquals("DATABASE_CONSTRAINT", error);
    // }

    // @Test
    // void testIllegalArgumentException() {
    //     IllegalArgumentException e = new IllegalArgumentException("Invalid input provided");

    //     Response response = mapper.toResponse(e);

    //     assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    //     ApiError error = (ApiError) response.getEntity();
    //     assertEquals("ILLEGAL_ARGUMENT", error);
    // }

    // @Test
    // void testUnhandledException() {
    //     Exception e = new RuntimeException("Unexpected error");

    //     Response response = mapper.toResponse(e);

    //     assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    //     ApiError error = (ApiError) response.getEntity();
    //     assertEquals("INTERNAL_ERROR", error);
    // }

    // @Test
    // void testResponseIncludesPath() {
    //     IllegalArgumentException e = new IllegalArgumentException("Test error");

    //     Response response = mapper.toResponse(e);
    //     ApiError error = (ApiError) response.getEntity();

    //     assertEquals("/api/vehicles", error);
    // }
}
