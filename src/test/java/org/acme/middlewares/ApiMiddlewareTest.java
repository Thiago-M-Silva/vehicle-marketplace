package org.acme.middlewares;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.acme.abstracts.Vehicles;
import org.acme.dtos.*;
import org.acme.interfaces.VehicleMapper;
import org.acme.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
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

    private JsonObject createBikeJson() {
        return Json.createObjectBuilder()
                .add("brand", "Yamaha")
                .add("model", "YZF")
                .add("year", 2021)
                .build();
    }

    @Nested
    class ManageVehiclesTypeRequestDTO_Single {

        @Test
        @DisplayName("Should map car JSON to Cars entity")
        void testManageVehiclesTypeRequestDTO_Cars() throws Exception {
            JsonObject carJson = createCarJson();
            CarsRequestDTO dto = new CarsRequestDTO();
            Cars car = new Cars();

            // Mock ObjectMapper behavior
            ObjectMapper spyMapper = spy(apiMiddleware.objectMapper);
            doReturn(dto).when(spyMapper).readValue(carJson.toString(), CarsRequestDTO.class);
            apiMiddleware.objectMapper = spyMapper;

            when(mapper.toCars(dto)).thenReturn(car);

            Vehicles result = apiMiddleware.manageVehiclesTypeRequestDTO("cars", carJson);

            assertSame(car, result);
            verify(mapper).toCars(dto);
        }

        @Test
        @DisplayName("Should map bike JSON to Bikes entity")
        void testManageVehiclesTypeRequestDTO_Bikes() throws Exception {
            JsonObject bikeJson = createBikeJson();
            BikesRequestDTO dto = new BikesRequestDTO();
            Bikes bike = new Bikes();

            ObjectMapper spyMapper = spy(apiMiddleware.objectMapper);
            doReturn(dto).when(spyMapper).readValue(bikeJson.toString(), BikesRequestDTO.class);
            apiMiddleware.objectMapper = spyMapper;

            when(mapper.toBikes(dto)).thenReturn(bike);

            Vehicles result = apiMiddleware.manageVehiclesTypeRequestDTO("bikes", bikeJson);

            assertSame(bike, result);
            verify(mapper).toBikes(dto);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException if body is null")
        void testManageVehiclesTypeRequestDTO_NullBody() {
            assertThrows(IllegalArgumentException.class, ()
                    -> apiMiddleware.manageVehiclesTypeRequestDTO("cars", null));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for invalid vehicle type")
        void testManageVehiclesTypeRequestDTO_InvalidType() {
            JsonObject json = createCarJson();
            assertThrows(IllegalArgumentException.class, ()
                    -> apiMiddleware.manageVehiclesTypeRequestDTO("invalid", json));
        }

        @Test
        @DisplayName("Should throw RuntimeException on JSON parse error")
        void testManageVehiclesTypeRequestDTO_JsonParseError() throws Exception {
            JsonObject carJson = createCarJson();
            ObjectMapper spyMapper = spy(apiMiddleware.objectMapper);
            doThrow(new RuntimeException("parse error")).when(spyMapper).readValue(anyString(), eq(CarsRequestDTO.class));
            apiMiddleware.objectMapper = spyMapper;

            assertThrows(RuntimeException.class, ()
                    -> apiMiddleware.manageVehiclesTypeRequestDTO("cars", carJson));
        }
    }

    @Nested
    class ManageVehiclesTypeRequestDTO_ListJsonObject {

        @Test
        @DisplayName("Should map list of JSON objects to list of Vehicles")
        void testManageVehiclesTypeRequestDTO_List() {
            JsonObject carJson = createCarJson();
            Cars car = new Cars();

            doReturn(car).when(mapper).toCars(any(CarsRequestDTO.class));
            ObjectMapper spyMapper = spy(apiMiddleware.objectMapper);
            try {
                doReturn(new CarsRequestDTO()).when(spyMapper).readValue(carJson.toString(), CarsRequestDTO.class);
            } catch (Exception ignored) {
            }
            apiMiddleware.objectMapper = spyMapper;

            List<JsonObject> jsonList = List.of(carJson, carJson);
            List<Vehicles> result = apiMiddleware.manageVehiclesTypeRequestDTO("cars", jsonList);

            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException if list is null")
        void testManageVehiclesTypeRequestDTO_ListNull() {
            assertThrows(IllegalArgumentException.class, ()
                    -> apiMiddleware.manageVehiclesTypeRequestDTO("cars", (List<JsonObject>) null));
        }
    }

    @Nested
    class ManageListVehiclesTypeRequestDTO_ListMap {

        @Test
        @DisplayName("Should map list of maps to list of Vehicles")
        void testManageListVehiclesTypeRequestDTO() {
            Map<String, Object> map = new HashMap<>();
            map.put("brand", "Toyota");
            map.put("model", "Corolla");
            map.put("year", 2020);

            Cars car = new Cars();
            doReturn(car).when(mapper).toCars(any(CarsRequestDTO.class));
            ObjectMapper spyMapper = spy(apiMiddleware.objectMapper);
            try {
                doReturn(new CarsRequestDTO()).when(spyMapper).readValue(anyString(), eq(CarsRequestDTO.class));
            } catch (Exception ignored) {
            }
            apiMiddleware.objectMapper = spyMapper;

            List<Map<String, Object>> mapList = List.of(map, map);
            List<Vehicles> result = apiMiddleware.manageListVehiclesTypeRequestDTO("cars", mapList);

            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException if list is null")
        void testManageListVehiclesTypeRequestDTO_Null() {
            assertThrows(IllegalArgumentException.class, ()
                    -> apiMiddleware.manageListVehiclesTypeRequestDTO("cars", null));
        }
    }

    @Nested
    class ManageVehicleTypeResponseDTO_List {

        @Test
        @DisplayName("Should map list of Cars to CarsDTO list")
        void testManageVehicleTypeResponseDTO_Cars() {
            List<Cars> carsList = List.of(new Cars(), new Cars());
            List<Object> carsDTOList = List.of(new Object(), new Object());
            when(mapper.toCarsDTOList(carsList)).thenReturn(carsDTOList);

            Object result = apiMiddleware.manageVehicleTypeResponseDTO("cars", carsList);

            assertSame(carsDTOList, result);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException if list is null")
        void testManageVehicleTypeResponseDTO_Null() {
            assertThrows(IllegalArgumentException.class, ()
                    -> apiMiddleware.manageVehicleTypeResponseDTO("cars", null));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for invalid type")
        void testManageVehicleTypeResponseDTO_InvalidType() {
            List<Cars> carsList = List.of(new Cars());
            assertThrows(IllegalArgumentException.class, ()
                    -> apiMiddleware.manageVehicleTypeResponseDTO("invalid", carsList));
        }
    }

    @Nested
    class ManageVehicleTypeResponseDTO_Single {

        @Test
        @DisplayName("Should map single Cars to CarsDTO")
        void testManageVehicleTypeResponseDTO_Cars() {
            Cars car = new Cars();
            Object carDTO = new Object();
            when(mapper.toCarsDTO(car)).thenReturn(carDTO);

            Object result = apiMiddleware.manageVehicleTypeResponseDTO("cars", car);

            assertSame(carDTO, result);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException if vehicle is null")
        void testManageVehicleTypeResponseDTO_Null() {
            assertThrows(IllegalArgumentException.class, ()
                    -> apiMiddleware.manageVehicleTypeResponseDTO("cars", null));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for invalid type")
        void testManageVehicleTypeResponseDTO_InvalidType() {
            Cars car = new Cars();
            assertThrows(IllegalArgumentException.class, ()
                    -> apiMiddleware.manageVehicleTypeResponseDTO("invalid", car));
        }
    }

    @Nested
    class ManageVehicleTypeResponseDTOList {

        @Test
        @DisplayName("Should map list of Vehicles to DTO list for cars")
        void testManageVehicleTypeResponseDTOList_Cars() {
            Cars car1 = new Cars();
            Cars car2 = new Cars();
            List<Vehicles> vehicles = List.of(car1, car2);
            List<Cars> cars = List.of(car1, car2);
            List<Object> carsDTOList = List.of(new Object(), new Object());

            when(mapper.toCarsDTOList(cars)).thenReturn(carsDTOList);

            List<?> result = apiMiddleware.manageVehicleTypeResponseDTOList("cars", vehicles);

            assertEquals(carsDTOList, result);
        }

        @Test
        @DisplayName("Should return empty list if input is null or empty")
        void testManageVehicleTypeResponseDTOList_EmptyOrNull() {
            assertTrue(apiMiddleware.manageVehicleTypeResponseDTOList("cars", null).isEmpty());
            assertTrue(apiMiddleware.manageVehicleTypeResponseDTOList("cars", List.of()).isEmpty());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for invalid type")
        void testManageVehicleTypeResponseDTOList_InvalidType() {
            List<Vehicles> vehicles = List.of(new Cars());
            assertThrows(IllegalArgumentException.class, ()
                    -> apiMiddleware.manageVehicleTypeResponseDTOList("invalid", vehicles));
        }
    }
}
