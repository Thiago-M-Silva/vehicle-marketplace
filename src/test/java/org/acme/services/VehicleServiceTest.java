package org.acme.services;

import org.acme.abstracts.Vehicles;
import org.acme.dtos.UsersRequestDTO;
import org.acme.dtos.VehicleSearchDTO;
import org.acme.enums.ECategory;
import org.acme.enums.EColors;
import org.acme.enums.EFuelType;
import org.acme.enums.EStatus;
import org.acme.interfaces.VehicleMapper;
import org.acme.model.Bikes;
import org.acme.model.VehicleDocuments;
import org.acme.repositories.BikesRepository;
import org.acme.repositories.BoatsRepository;
import org.acme.repositories.CarsRepository;
import org.acme.repositories.PlanesRepository;
import org.acme.repositories.VehicleDocumentsRepository;
import org.bson.types.ObjectId;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

import org.acme.dtos.UsersResponseDTO;
import org.acme.model.Users;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehicleServiceTest {

    @InjectMocks
    VehicleService vehicleService;

    @Mock
    BikesRepository bikesRepository;
    @Mock
    CarsRepository carsRepository;
    @Mock
    BoatsRepository boatsRepository;
    @Mock
    PlanesRepository planesRepository;
    @Mock
    GridFSService gridFSService;
    @Mock
    VehicleDocumentsRepository vehicleDocumentsRepository;
    @Mock
    VehicleMapper vehicleMapper;
    @Mock
    UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vehicleService.bikesRepository = bikesRepository;
        vehicleService.carsRepository = carsRepository;
        vehicleService.boatsRepository = boatsRepository;
        vehicleService.planesRepository = planesRepository;
        vehicleService.gridFSService = gridFSService;
        vehicleService.repository = vehicleDocumentsRepository;
        vehicleService.vehicleMapper = vehicleMapper;
        vehicleService.userService = userService;
    }

    @Test
    void testListAll_ReturnsVehicles() {
        List<Bikes> bikes = List.of(mock(Bikes.class));
        when(bikesRepository.listAll()).thenReturn(bikes);

        List<Vehicles> result = vehicleService.listAll("bikes");

        assertEquals(bikes, result);
        verify(bikesRepository).listAll();
    }

    @Test
    void testListAll_ThrowsRuntimeException() {
        when(bikesRepository.listAll()).thenThrow(new RuntimeException("DB error"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> vehicleService.listAll("bikes"));
        assertTrue(ex.getMessage().contains("Failed to list vehicles"));
    }

    @Test
    void testFindById_ReturnsVehicle() {
        UUID id = UUID.randomUUID();
        Bikes bike = mock(Bikes.class);
        when(bikesRepository.findById(id)).thenReturn(bike);

        Bikes result = vehicleService.findById("bikes", id);

        assertEquals(bike, result);
        verify(bikesRepository).findById(id);
    }

    @Test
    void testFindById_NullId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> vehicleService.findById("bikes", null));
    }

    @Test
    void testSave_PersistsVehicle() {
        Bikes bike = mock(Bikes.class);
        BikesRepository repo = bikesRepository;
        doNothing().when(repo).persist(bike);

        Bikes result = vehicleService.save("bikes", bike);

        assertEquals(bike, result);
        verify(repo).persist(bike);
    }

    @Test
    void testSave_NullVehicle_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> vehicleService.save("bikes", null));
    }

    @Test
    void testSaveMultipleVehicles_PersistsList() {
        Bikes bike1 = mock(Bikes.class);
        Bikes bike2 = mock(Bikes.class);
        List<Vehicles> bikes = List.of(bike1, bike2);

        doNothing().when(bikesRepository).persist((Bikes) any(Vehicles.class));

        int count = vehicleService.saveMultipleVehicles("bikes", bikes);

        assertEquals(2, count);
        verify(bikesRepository, times(2)).persist((Bikes) any(Vehicles.class));
    }

    @Test
    void testSaveMultipleVehicles_NullOrEmpty_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> vehicleService.saveMultipleVehicles("bikes", null));
        assertThrows(IllegalArgumentException.class, () -> vehicleService.saveMultipleVehicles("bikes", List.of()));
    }

    @Test
    void testSaveVehicleWithDocuments_WithDocument() {
        Bikes bike = mock(Bikes.class);
        UUID id = UUID.randomUUID();
        when(bike.getId()).thenReturn(id);
        doNothing().when(bikesRepository).persist(bike);
        doReturn(bike).when(bikesRepository).findById(id);

        InputStream is = new ByteArrayInputStream("test".getBytes());
        doReturn(bike).when(vehicleService).save("bikes", bike);
        doReturn(new VehicleDocuments()).when(vehicleService)
                .saveDocument(eq(id), anyString(), anyString(), any(InputStream.class));

        Bikes result = vehicleService.saveVehicleWithDocuments("bikes", bike, is, "file.pdf", "application/pdf");

        assertEquals(bike, result);
    }

    @Test
    void testSaveVehicleWithDocuments_WithoutDocument() {
        Bikes bike = mock(Bikes.class);
        doReturn(bike).when(vehicleService).save("bikes", bike);

        Bikes result = vehicleService.saveVehicleWithDocuments("bikes", bike, null, null, null);

        assertEquals(bike, result);
    }

    @Test
    void testSaveDocument_Success() throws Exception {
        UUID vehicleId = UUID.randomUUID();
        String filename = "doc.pdf";
        String contentType = "application/pdf";
        InputStream is = new ByteArrayInputStream("test".getBytes());
        ObjectId objectId = new ObjectId();
        when(gridFSService.uploadFile(eq(filename), eq(contentType), any(InputStream.class))).thenReturn(objectId);

        VehicleDocuments doc = vehicleService.saveDocument(vehicleId, filename, contentType, is);

        assertEquals(filename, doc.fileName);
        assertEquals(contentType, doc.contentType);
        assertEquals(vehicleId.toString(), doc.vehicleId);
        assertNotNull(doc.id);
        verify(vehicleDocumentsRepository).persist(any(VehicleDocuments.class));
    }

    @Test
    void testSaveDocument_UploadFails_ThrowsException() throws Exception {
        InputStream is = new ByteArrayInputStream("test".getBytes());
        when(gridFSService.uploadFile(anyString(), anyString(), any(InputStream.class)))
                .thenThrow(new RuntimeException("upload failed"));

        assertThrows(RuntimeException.class, ()
                -> vehicleService.saveDocument(UUID.randomUUID(), "f", "c", is));
    }

    @Test
    void testDeleteById_Success() {
        UUID id = UUID.randomUUID();
        when(bikesRepository.deleteById(id)).thenReturn(true);

        boolean result = vehicleService.deleteById("bikes", id);

        assertTrue(result);
        verify(bikesRepository).deleteById(id);
    }

    @Test
    void testDeleteById_NullId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> vehicleService.deleteById("bikes", null));
    }

    @Test
    void testDeleteManyVehicles_Success() {
        List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID());
        when(bikesRepository.delete(anyString(), eq(ids))).thenReturn(2L);

        Long result = vehicleService.deleteManyVehicles("bikes", ids);

        assertEquals(2L, result);
        verify(bikesRepository).delete("id in ?1", ids);
    }

    @Test
    void testDeleteManyVehicles_EmptyList_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> vehicleService.deleteManyVehicles("bikes", List.of()));
    }

    @Test
    void testEditVehicleInfo_UnknownType_ThrowsException() {
        UUID id = UUID.randomUUID();
        Vehicles vehicle = mock(Vehicles.class);
        when(vehicleService.findById("unknown", id)).thenReturn(vehicle);

        assertThrows(IllegalArgumentException.class, ()
                -> vehicleService.editVehicleInfo("unknown", id, vehicle));
    }

    @Test
    void testEditVehicleInfo_NullVehicle_ThrowsException() {
        assertThrows(IllegalArgumentException.class, ()
                -> vehicleService.editVehicleInfo("bikes", UUID.randomUUID(), null));
    }

    @Test
    void testEditVehicleInfo_VehicleNotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(vehicleService.findById("bikes", id)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, ()
                -> vehicleService.editVehicleInfo("bikes", id, mock(Bikes.class)));
    }

    @Test
    void testUpdateVehicleSold_Success() {
        UUID id = UUID.randomUUID();
        String email = "test@example.com";
        Vehicles vehicle = mock(Vehicles.class);
        Object customerDto = mock(Object.class);
        Object buyer = mock(Object.class);

        when(vehicleService.findById("bikes", id)).thenReturn(vehicle);
        when(userService.getUserByEmail(email)).thenReturn((UsersResponseDTO) customerDto);
        doNothing().when(vehicle).setOwner((Users) buyer);
        doNothing().when(userService).editUser(any(), any());

        assertDoesNotThrow(() -> vehicleService.updateVehicleSold("bikes", id, email));
        verify(vehicle).setOwner((Users) buyer);
        verify(userService).editUser(any(), any());
    }

    @Test
    void testUpdateVehicleSold_NullOrBlankEmail_ThrowsException() {
        assertThrows(IllegalArgumentException.class, ()
                -> vehicleService.updateVehicleSold("bikes", UUID.randomUUID(), null));
        assertThrows(IllegalArgumentException.class, ()
                -> vehicleService.updateVehicleSold("bikes", UUID.randomUUID(), " "));
    }

    @Test
    void testUpdateVehicleSold_VehicleNotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(vehicleService.findById("bikes", id)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, ()
                -> vehicleService.updateVehicleSold("bikes", id, "a@b.com"));
    }

    @Test
    void testUpdateVehicleSold_CustomerNotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(vehicleService.findById("bikes", id)).thenReturn(mock(Vehicles.class));
        when(userService.getUserByEmail(anyString())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, ()
                -> vehicleService.updateVehicleSold("bikes", id, "a@b.com"));
    }

    @Test
    void testSearchVehicle_WithAllFilters() {
        VehicleSearchDTO dto = new VehicleSearchDTO();
        dto.setBrand("Yamaha");
        dto.setModel("MT");
        dto.setYearMin(2015);
        dto.setYearMax(2020);
        dto.setPriceMin(new BigDecimal(10000.0));
        dto.setPriceMax(new BigDecimal(20000.0));
        dto.setCategory(ECategory.MONOCYCLE.name());
        dto.setColor(EColors.BLACK.name());
        dto.setFuelType(EFuelType.GASOLINE.name());
        dto.setOwnerId(UUID.randomUUID().toString());
        dto.setVehicleStatus(EStatus.NEW.name());
        dto.setSortBy("price");
        dto.setDirection("DESC");
        dto.setPage(1);
        dto.setSize(5);

        var panacheQuery = mock(PanacheQuery.class);
        List<Bikes> expected = List.of(mock(Bikes.class));

        when(bikesRepository.find(anyString(), any(Sort.class), anyMap())).thenReturn((PanacheQuery<Bikes>) panacheQuery);
        when(((PanacheQuery<Bikes>) panacheQuery).page(any(Page.class))).thenReturn((PanacheQuery<Bikes>) panacheQuery);
        when(((PanacheQuery<Bikes>) panacheQuery).list()).thenReturn(expected);

        List<? extends Vehicles> result = vehicleService.searchVehicle("bikes", dto);

        assertEquals(expected, result);

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        ArgumentCaptor<Map<String, Object>> paramsCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Page> pageCaptor = ArgumentCaptor.forClass(Page.class);

        verify(bikesRepository).find(queryCaptor.capture(), sortCaptor.capture(), paramsCaptor.capture());
        ((PanacheQuery<Bikes>) verify(panacheQuery)).page(pageCaptor.capture());
        ((PanacheQuery<Bikes>) verify(panacheQuery)).list();

        assertTrue(queryCaptor.getValue().contains("AND brand = :brand"));
        assertTrue(queryCaptor.getValue().contains("AND model ILIKE :model"));
        assertEquals(Sort.descending("price"), sortCaptor.getValue());
        assertEquals(1, pageCaptor.getValue().index);
        assertEquals(5, pageCaptor.getValue().size);
    }
}
