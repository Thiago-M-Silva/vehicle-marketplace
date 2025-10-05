package org.acme.middlewares;

import org.acme.abstracts.Vehicles;
import org.acme.dtos.*;
import org.acme.interfaces.VehicleMapper;
import org.acme.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ApiMiddlewareTest {

    @Mock
    VehicleMapper mapper;

    @InjectMocks
    ApiMiddleware apiMiddleware;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        apiMiddleware = new ApiMiddleware();
        apiMiddleware.mapper = mapper;
    }

    private JsonObject createCarJson() {
        return Json.createObjectBuilder()
                .add("brand", "Toyota")
                .add("model", "Corolla")
                .add("year", 2020)
                .build();
    }

    @Test
    void manageVehiclesTypeRequestDTO_shouldMapCar() throws Exception {
        JsonObject carJson = createCarJson();
        CarsRequestDTO dto = new CarsRequestDTO();
        Cars car = new Cars();

        ApiMiddleware spy = Mockito.spy(apiMiddleware);
        doReturn(dto).when(spy).objectMapper.readValue(carJson.toString(), CarsRequestDTO.class);
        when(mapper.toCars(dto)).thenReturn(car);

        Vehicles result = spy.manageVehiclesTypeRequestDTO("cars", carJson);
        assertEquals(car, result);
        verify(mapper).toCars(dto);
    }

    @Test
    void manageVehiclesTypeRequestDTO_shouldThrowOnNullBody() {
        assertThrows(IllegalArgumentException.class, () ->
                apiMiddleware.manageVehiclesTypeRequestDTO("cars", (jakarta.json.JsonObject) null));
    }

    @Test
    void manageVehiclesTypeRequestDTO_shouldThrowOnInvalidType() {
        JsonObject carJson = createCarJson();
        assertThrows(IllegalArgumentException.class, () ->
                apiMiddleware.manageVehiclesTypeRequestDTO("invalid", carJson));
    }

    @Test
    void manageVehiclesTypeRequestDTO_List_shouldMapList() throws JsonMappingException, JsonProcessingException {
        JsonObject carJson = createCarJson();
        CarsRequestDTO dto = new CarsRequestDTO();
        Cars car = new Cars();

        ApiMiddleware spy = Mockito.spy(apiMiddleware);
        doReturn(dto).when(spy).objectMapper.readValue(carJson.toString(), CarsRequestDTO.class);
        when(mapper.toCars(dto)).thenReturn(car);

        List<JsonObject> list = List.of(carJson, carJson);
        List<Vehicles> result = spy.manageVehiclesTypeRequestDTO("cars", list);

        assertEquals(2, result.size());
        assertEquals(car, result.get(0));
        verify(mapper, times(2)).toCars(dto);
    }

    @Test
    void manageVehiclesTypeRequestDTO_List_shouldThrowOnNull() {
        assertThrows(IllegalArgumentException.class, () ->
                apiMiddleware.manageVehiclesTypeRequestDTO("cars", (List<JsonObject>) null));
    }

    @Test
    void manageListVehiclesTypeRequestDTO_shouldMapList() throws JsonMappingException, JsonProcessingException {
        Map<String, Object> carMap = new HashMap<>();
        carMap.put("brand", "Toyota");
        carMap.put("model", "Corolla");
        carMap.put("year", 2020);

        CarsRequestDTO dto = new CarsRequestDTO();
        Cars car = new Cars();

        ApiMiddleware spy = Mockito.spy(apiMiddleware);
        doReturn(dto).when(spy).objectMapper.readValue(anyString(), eq(CarsRequestDTO.class));
        when(mapper.toCars(dto)).thenReturn(car);

        List<Map<String, Object>> list = List.of(carMap, carMap);
        List<Vehicles> result = spy.manageListVehiclesTypeRequestDTO("cars", list);

        assertEquals(2, result.size());
        verify(mapper, times(2)).toCars(dto);
    }

    @Test
    void manageListVehiclesTypeRequestDTO_shouldThrowOnNull() {
        assertThrows(IllegalArgumentException.class, () ->
                apiMiddleware.manageListVehiclesTypeRequestDTO("cars", null));
    }

    @Test
    void manageVehicleTypeResponseDTO_shouldMapListToDTO() {
        List<Cars> carsList = List.of(new Cars(), new Cars());
        List<CarsResponseDTO> dtoList = List.of(new CarsResponseDTO(), new CarsResponseDTO());
        when(mapper.toCarsDTOList(carsList)).thenReturn(dtoList);

        Object result = apiMiddleware.manageVehicleTypeResponseDTO("cars", carsList);
        assertEquals(dtoList, result);
        verify(mapper).toCarsDTOList(carsList);
    }

    @Test
    void manageVehicleTypeResponseDTO_shouldThrowOnNullList() {
        assertThrows(IllegalArgumentException.class, () ->
                apiMiddleware.manageVehicleTypeResponseDTO("cars", (List<? extends Vehicles>) null));
    }

    @Test
    void manageVehicleTypeResponseDTO_shouldThrowOnInvalidType() {
        List<Cars> carsList = List.of(new Cars());
        assertThrows(IllegalArgumentException.class, () ->
                apiMiddleware.manageVehicleTypeResponseDTO("invalid", carsList));
    }

    @Test
    void manageVehicleTypeResponseDTO_single_shouldMapToDTO() {
        Cars car = new Cars();
        Object dto = new Object();
        when(mapper.toCarsDTO(car)).thenReturn((CarsResponseDTO) dto);

        Object result = apiMiddleware.manageVehicleTypeResponseDTO("cars", car);
        assertEquals(dto, result);
        verify(mapper).toCarsDTO(car);
    }

    @Test
    void manageVehicleTypeResponseDTO_single_shouldThrowOnNull() {
        assertThrows(IllegalArgumentException.class, () ->
                apiMiddleware.manageVehicleTypeResponseDTO("cars", (Vehicles) null));
    }

    @Test
    void manageVehicleTypeResponseDTO_single_shouldThrowOnInvalidType() {
        Cars car = new Cars();
        assertThrows(IllegalArgumentException.class, () ->
                apiMiddleware.manageVehicleTypeResponseDTO("invalid", car));
    }

    @Test
    void manageVehicleTypeResponseDTOList_shouldReturnEmptyOnNullOrEmpty() {
        assertTrue(apiMiddleware.manageVehicleTypeResponseDTOList("cars", null).isEmpty());
        assertTrue(apiMiddleware.manageVehicleTypeResponseDTOList("cars", List.of()).isEmpty());
    }

    @Test
    void manageVehicleTypeResponseDTOList_shouldMapFilteredList() {
        Cars car = new Cars();
        List<Vehicles> vehicles = List.of(car, new Cars());
        List<CarsResponseDTO> dtoList = List.of(new CarsResponseDTO());
        when(mapper.toCarsDTOList(List.of(car))).thenReturn(dtoList);

        List<?> result = apiMiddleware.manageVehicleTypeResponseDTOList("cars", vehicles);
        assertEquals(dtoList, result);
        verify(mapper).toCarsDTOList(List.of(car));
    }

    @Test
    void manageVehicleTypeResponseDTOList_shouldThrowOnInvalidType() {
        List<Vehicles> vehicles = List.of(new Cars());
        assertThrows(IllegalArgumentException.class, () ->
                apiMiddleware.manageVehicleTypeResponseDTOList("invalid", vehicles));
    }
}