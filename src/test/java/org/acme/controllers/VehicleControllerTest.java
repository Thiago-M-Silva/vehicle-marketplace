
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.io.ByteArrayOutputStream;
import java.util.*;
import org.acme.abstracts.Vehicles;
import org.acme.dtos.VehicleSearchDTO;
import org.acme.middlewares.ApiMiddleware;
import org.acme.services.GridFSService;
import org.acme.services.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;



package org.acme.controllers;





@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    @Mock
    private VehicleService vehicleService;

    @Mock
    private ApiMiddleware apiMiddleware;

    @Mock
    private GridFSService gridFSService;

    @InjectMocks
    private VehicleController vehicleController;

    private UUID testId;
    private String vehicleType;
    private Vehicles mockVehicle;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        vehicleType = "car";
        mockVehicle = mock(Vehicles.class);
    }

    @Test
    void testGetAllVehiclesSuccess() {
        List<Vehicles> vehicles = Arrays.asList(mockVehicle);
        List<Object> responseDTOs = Arrays.asList(new Object());

        when(vehicleService.listAll(vehicleType)).thenReturn(vehicles);
        when(apiMiddleware.manageVehicleTypeResponseDTO(vehicleType, vehicles)).thenReturn(responseDTOs);

        Response response = vehicleController.getAllVehicles(vehicleType);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(vehicleService).listAll(vehicleType);
    }

    @Test
    void testGetAllVehiclesException() {
        when(vehicleService.listAll(vehicleType)).thenThrow(new RuntimeException("DB Error"));

        Response response = vehicleController.getAllVehicles(vehicleType);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error retrieving vehicles"));
    }

    @Test
    void testDownloadSuccess() {
        String vehicleId = "doc123";

        doNothing().when(gridFSService).downloadFile(eq(vehicleId), any(ByteArrayOutputStream.class));

        Response response = vehicleController.download(vehicleId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(gridFSService).downloadFile(eq(vehicleId), any(ByteArrayOutputStream.class));
    }

    @Test
    void testGetVehiclesByIdSuccess() {
        when(vehicleService.findById(vehicleType, testId)).thenReturn(mockVehicle);

        Response response = vehicleController.getVehiclesById(vehicleType, testId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(vehicleService).findById(vehicleType, testId);
    }

    @Test
    void testGetVehiclesByIdNotFound() {
        when(vehicleService.findById(vehicleType, testId)).thenThrow(new RuntimeException("Not found"));

        Response response = vehicleController.getVehiclesById(vehicleType, testId);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Vehicle not found"));
    }

    @Test
    void testSearchSuccess() {
        VehicleSearchDTO searchParams = new VehicleSearchDTO();
        List<Vehicles> results = Arrays.asList(mockVehicle);

        when(vehicleService.searchVehicle(vehicleType, searchParams)).thenReturn(results);

        Response response = vehicleController.search(vehicleType, searchParams);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(vehicleService).searchVehicle(vehicleType, searchParams);
    }

    @Test
    void testSearchException() {
        VehicleSearchDTO searchParams = new VehicleSearchDTO();

        when(vehicleService.searchVehicle(vehicleType, searchParams)).thenThrow(new RuntimeException("Search error"));

        Response response = vehicleController.search(vehicleType, searchParams);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Searching error"));
    }

    @Test
    void testSaveAllVehiclesSuccess() {
        List<Map<String, Object>> vehicles = Arrays.asList(new HashMap<>());
        List<Object> vehiclesRequestDTO = Arrays.asList(new Object());

        when(apiMiddleware.manageListVehiclesTypeRequestDTO(vehicleType, vehicles)).thenReturn(vehiclesRequestDTO);
        when(vehicleService.saveMultipleVehicles(vehicleType, vehiclesRequestDTO)).thenReturn(1);

        Response response = vehicleController.saveAllVehicles(vehicleType, vehicles);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        verify(vehicleService).saveMultipleVehicles(vehicleType, vehiclesRequestDTO);
    }

    @Test
    void testSaveAllVehiclesException() {
        List<Map<String, Object>> vehicles = Arrays.asList(new HashMap<>());
        List<Object> vehiclesRequestDTO = Arrays.asList(new Object());

        when(apiMiddleware.manageListVehiclesTypeRequestDTO(vehicleType, vehicles)).thenReturn(vehiclesRequestDTO);
        when(vehicleService.saveMultipleVehicles(vehicleType, vehiclesRequestDTO)).thenThrow(new RuntimeException("Save error"));

        Response response = vehicleController.saveAllVehicles(vehicleType, vehicles);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error saving vehicle"));
    }

    @Test
    void testAddVehicleSuccess() {
        JsonObject body = mock(JsonObject.class);

        when(apiMiddleware.manageVehiclesTypeRequestDTO(vehicleType, body)).thenReturn(mockVehicle);
        when(vehicleService.save(vehicleType, mockVehicle)).thenReturn(mockVehicle);

        Response response = vehicleController.addVehicle(vehicleType, body);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        verify(vehicleService).save(vehicleType, mockVehicle);
    }

    @Test
    void testAddVehicleException() {
        JsonObject body = mock(JsonObject.class);

        when(apiMiddleware.manageVehiclesTypeRequestDTO(vehicleType, body)).thenThrow(new RuntimeException("Invalid data"));

        Response response = vehicleController.addVehicle(vehicleType, body);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error saving vehicle"));
    }

    @Test
    void testDeleteVehicleSuccess() {
        doNothing().when(vehicleService).deleteById(vehicleType, testId);

        Response response = vehicleController.deleteVehicle(vehicleType, testId);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(vehicleService).deleteById(vehicleType, testId);
    }

    @Test
    void testDeleteVehicleException() {
        doThrow(new RuntimeException("Delete error")).when(vehicleService).deleteById(vehicleType, testId);

        Response response = vehicleController.deleteVehicle(vehicleType, testId);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error deleting vehicle"));
    }

    @Test
    void testDeleteManyVehiclesSuccess() {
        List<UUID> ids = Arrays.asList(testId);

        doNothing().when(vehicleService).deleteManyVehicles(vehicleType, ids);

        Response response = vehicleController.deleteManyVehicles(vehicleType, ids);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(vehicleService).deleteManyVehicles(vehicleType, ids);
    }

    @Test
    void testDeleteManyVehiclesException() {
        List<UUID> ids = Arrays.asList(testId);

        doThrow(new RuntimeException("Delete error")).when(vehicleService).deleteManyVehicles(vehicleType, ids);

        Response response = vehicleController.deleteManyVehicles(vehicleType, ids);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error deleting vehicle"));
    }

    @Test
    void testEditVehicleSuccess() {
        JsonObject body = mock(JsonObject.class);

        when(apiMiddleware.manageVehiclesTypeRequestDTO(vehicleType, body)).thenReturn(mockVehicle);
        doNothing().when(vehicleService).editVehicleInfo(vehicleType, testId, mockVehicle);

        Response response = vehicleController.editVehicle(vehicleType, testId, body);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(vehicleService).editVehicleInfo(vehicleType, testId, mockVehicle);
    }

    @Test
    void testEditVehicleException() {
        JsonObject body = mock(JsonObject.class);

        when(apiMiddleware.manageVehiclesTypeRequestDTO(vehicleType, body)).thenThrow(new RuntimeException("Edit error"));

        Response response = vehicleController.editVehicle(vehicleType, testId, body);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error editing vehicle"));
    }
}
