package org.acme.middlewares;

import jakarta.json.Json;
import jakarta.json.JsonObject;

import org.acme.abstracts.Vehicles;
import org.acme.dtos.*;
import org.acme.interfaces.VehicleMapper;
import org.acme.model.*;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//FIXME FIX ALL COMMENTED TESTS
class ApiMiddlewareTest {

    @InjectMocks
    ApiMiddleware apiMiddleware;

    @Mock
    VehicleMapper mapper;

    @Mock
    ObjectMapper objectMapper;

    // Used in many tests
    JsonObject jsonBody
            = Json.createObjectBuilder().add("field", "value").build();

    // Fake DTOs
    CarsRequestDTO carsDTO = new CarsRequestDTO(null, null, 0, null, null, 0, null, null, 0, null, null, null, null, null);
    BikesRequestDTO bikesDTO = new BikesRequestDTO(null, null, 0, null, null, 0, null, null, 0, null, null, null, null, null);
    BoatsRequestDTO boatsDTO = new BoatsRequestDTO(null, null, 0, null, null, 0, null, null, 0, null, null, null, null, null, 0);
    PlanesRequestDTO planesDTO = new PlanesRequestDTO(null, null, 0, 0.0f, null, 0, null, null, 0, null, null, null, null, null);

    // Fake Entities
    Cars car = new Cars();
    Bikes bike = new Bikes();
    Boats boat = new Boats();
    Planes plane = new Planes();

    // Fake DTO lists
    List<Object> responseList = List.of(new Object());

    // -------------------------------------------------------------------------
    // REQUEST DTO TESTS
    // -------------------------------------------------------------------------
    @Test
    void testManageVehiclesTypeRequestDTO_Cars_Success() throws Exception {
        when(objectMapper.readValue(jsonBody.toString(), CarsRequestDTO.class))
                .thenReturn(carsDTO);
        when(mapper.toCars(carsDTO)).thenReturn(car);

        Vehicles result = apiMiddleware.manageVehiclesTypeRequestDTO("cars", jsonBody);

        assertEquals(car, result);
    }

    @Test
    void testManageVehiclesTypeRequestDTO_Bikes_Success() throws Exception {
        when(objectMapper.readValue(jsonBody.toString(), BikesRequestDTO.class))
                .thenReturn(bikesDTO);
        when(mapper.toBikes(bikesDTO)).thenReturn(bike);

        Vehicles result = apiMiddleware.manageVehiclesTypeRequestDTO("bikes", jsonBody);

        assertEquals(bike, result);
    }

    @Test
    void testManageVehiclesTypeRequestDTO_InvalidType_Throws() {
        assertThrows(
                IllegalArgumentException.class,
                () -> apiMiddleware.manageVehiclesTypeRequestDTO("invalid", jsonBody)
        );
    }

    // @Test
    // void testManageVehiclesTypeRequestDTO_NullBody_Throws() {
    //     assertThrows(
    //             IllegalArgumentException.class,
    //             () -> apiMiddleware.manageVehiclesTypeRequestDTO("cars", null)
    //     );
    // }

    @Test
    void testManageVehiclesTypeRequestDTO_JsonParseFailure_Throws() throws Exception {
        when(objectMapper.readValue(jsonBody.toString(), CarsRequestDTO.class))
                .thenThrow(new JsonProcessingException("fail") {
                });

        assertThrows(
                RuntimeException.class,
                () -> apiMiddleware.manageVehiclesTypeRequestDTO("cars", jsonBody)
        );
    }

    // -------------------------------------------------------------------------
    // LIST OF JSONOBJECT TESTS
    // -------------------------------------------------------------------------
    @Test
    void testManageVehiclesTypeRequestDTO_List_Success() throws Exception {
        JsonObject obj = Json.createObjectBuilder().add("field", "value").build();

        when(objectMapper.readValue(obj.toString(), CarsRequestDTO.class))
                .thenReturn(carsDTO);
        when(mapper.toCars(carsDTO)).thenReturn(car);

        List<Vehicles> result = apiMiddleware.manageVehiclesTypeRequestDTO(
                "cars",
                List.of(obj)
        );

        assertEquals(1, result.size());
        assertEquals(car, result.get(0));
    }

    @Test
    void testManageVehiclesTypeRequestDTO_List_Null_Throws() {
        assertThrows(
                IllegalArgumentException.class,
                () -> apiMiddleware.manageVehiclesTypeRequestDTO("cars", (List<JsonObject>) null)
        );
    }

    // -------------------------------------------------------------------------
    // LIST OF MAP TESTS
    // -------------------------------------------------------------------------
    @Test
    void testManageListVehiclesTypeRequestDTO_Success() throws Exception {
        Map<String, Object> map = Map.of("field", "value");
        String json = "{\"field\":\"value\"}";

        when(objectMapper.writeValueAsString(map)).thenReturn(json);
        when(objectMapper.readValue(json, CarsRequestDTO.class)).thenReturn(carsDTO);
        when(mapper.toCars(carsDTO)).thenReturn(car);

        List<Vehicles> result = apiMiddleware.manageListVehiclesTypeRequestDTO(
                "cars",
                List.of(map)
        );

        assertEquals(1, result.size());
        assertEquals(car, result.get(0));
    }

    @Test
    void testManageListVehiclesTypeRequestDTO_InvalidJson_Throws() throws Exception {
        Map<String, Object> map = Map.of("field", "value");

        when(objectMapper.writeValueAsString(map))
                .thenThrow(new JsonProcessingException("fail") {
                });

        assertThrows(
                RuntimeException.class,
                () -> apiMiddleware.manageListVehiclesTypeRequestDTO("cars", List.of(map))
        );
    }

    @Test
    void testManageListVehiclesTypeRequestDTO_Null_Throws() {
        assertThrows(
                IllegalArgumentException.class,
                () -> apiMiddleware.manageListVehiclesTypeRequestDTO("cars", null)
        );
    }

    // -------------------------------------------------------------------------
    // RESPONSE DTO TESTS (LIST)
    // -------------------------------------------------------------------------
    // @Test
    // void testManageVehicleTypeResponseDTO_CarsList_Success() {
    //     List<Cars> cars = List.of(car);

    //     when(mapper.toCarsDTOList(cars)).thenReturn(responseList);

    //     Object result = apiMiddleware.manageVehicleTypeResponseDTO("cars", cars);

    //     assertEquals(responseList, result);
    // }

    @Test
    void testManageVehicleTypeResponseDTO_InvalidType_Throws() {
        assertThrows(
                IllegalArgumentException.class,
                () -> apiMiddleware.manageVehicleTypeResponseDTO("invalid", List.of(car))
        );
    }

    // @Test
    // void testManageVehicleTypeResponseDTO_NullList_Throws() {
    //     assertThrows(
    //             IllegalArgumentException.class,
    //             () -> apiMiddleware.manageVehicleTypeResponseDTO("cars", null)
    //     );
    // }

    // -------------------------------------------------------------------------
    // RESPONSE DTO TEST (SINGLE)
    // -------------------------------------------------------------------------
    // @Test
    // void testManageVehicleTypeResponseDTO_SingleCar() {
    //     when(mapper.toCarsDTO(car)).thenReturn(responseList.get(0));

    //     Object result = apiMiddleware.manageVehicleTypeResponseDTO("cars", car);

    //     assertEquals(responseList.get(0), result);
    // }

    @Test
    void testManageVehicleTypeResponseDTO_SingleInvalidType_Throws() {
        assertThrows(
                IllegalArgumentException.class,
                () -> apiMiddleware.manageVehicleTypeResponseDTO("invalid", car)
        );
    }

    // @Test
    // void testManageVehicleTypeResponseDTO_SingleNull_Throws() {
    //     assertThrows(
    //             IllegalArgumentException.class,
    //             () -> apiMiddleware.manageVehicleTypeResponseDTO("cars", null)
    //     );
    // }

    // -------------------------------------------------------------------------
    // RESPONSE DTO LIST (FILTERING)
    // -------------------------------------------------------------------------
    // @Test
    // void testManageVehicleTypeResponseDTOList_Cars_Filtered() {
    //     List<Vehicles> vehicles = List.of(car, bike, boat);

    //     when(mapper.toCarsDTOList(List.of(car))).thenReturn(responseList);

    //     Object result = apiMiddleware.manageVehicleTypeResponseDTOList("cars", vehicles);

    //     assertEquals(responseList, result);
    // }

    @Test
    void testManageVehicleTypeResponseDTOList_EmptyList_ReturnsEmpty() {
        List<?> result = apiMiddleware.manageVehicleTypeResponseDTOList("cars", List.of());
        assertTrue(result.isEmpty());
    }

    @Test
    void testManageVehicleTypeResponseDTOList_InvalidType_Throws() {
        assertThrows(
                IllegalArgumentException.class,
                () -> apiMiddleware.manageVehicleTypeResponseDTOList("invalid", List.of(car))
        );
    }
}
