package org.acme.services;

import io.quarkus.test.junit.QuarkusTest;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;
import org.acme.abstracts.Vehicles;
import org.acme.repositories.BikesRepository;
import org.acme.repositories.BoatsRepository;
import org.acme.repositories.CarsRepository;
import org.acme.repositories.PlanesRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

@QuarkusTest
class VehicleServiceTest {

    private VehicleService vehicleService;
    private BikesRepository bikesRepository;
    private CarsRepository carsRepository;
    private BoatsRepository boatsRepository;
    private PlanesRepository planesRepository;

    @BeforeEach
    void setUp() {
        vehicleService = new VehicleService();
        bikesRepository = mock(BikesRepository.class);
        carsRepository = mock(CarsRepository.class);
        boatsRepository = mock(BoatsRepository.class);
        planesRepository = mock(PlanesRepository.class);

        // Inject mocks via reflection since @Inject is not processed in unit tests
        setField(vehicleService, "bikesRepository", bikesRepository);
        setField(vehicleService, "carsRepository", carsRepository);
        setField(vehicleService, "boatsRepository", boatsRepository);
        setField(vehicleService, "planesRepository", planesRepository);
    }

    @Test
    void testGetRepositoryReturnsBikesRepository() {
        PanacheRepositoryBase<? extends Vehicles, UUID> repo = invokeGetRepository("bikes");
        assertSame(bikesRepository, repo);
    }

    @Test
    void testGetRepositoryReturnsCarsRepository() {
        PanacheRepositoryBase<? extends Vehicles, UUID> repo = invokeGetRepository("cars");
        assertSame(carsRepository, repo);
    }

    @Test
    void testGetRepositoryReturnsBoatsRepository() {
        PanacheRepositoryBase<? extends Vehicles, UUID> repo = invokeGetRepository("boats");
        assertSame(boatsRepository, repo);
    }

    @Test
    void testGetRepositoryReturnsPlanesRepository() {
        PanacheRepositoryBase<? extends Vehicles, UUID> repo = invokeGetRepository("planes");
        assertSame(planesRepository, repo);
    }

    @Test
    void testGetRepositoryIsCaseInsensitive() {
        assertSame(bikesRepository, invokeGetRepository("Bikes"));
        assertSame(carsRepository, invokeGetRepository("CARS"));
        assertSame(boatsRepository, invokeGetRepository("BoAtS"));
        assertSame(planesRepository, invokeGetRepository("pLaNeS"));
    }

    @Test
    void testGetRepositoryThrowsOnUnknownType() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            invokeGetRepository("trucks");
        });
        assertTrue(ex.getMessage().contains("Unknown vehicle type"));
    }

    // Helper to invoke private getRepository method via reflection
    @SuppressWarnings("unchecked")
    private PanacheRepositoryBase<? extends Vehicles, UUID> invokeGetRepository(String type) {
        try {
            var method = VehicleService.class.getDeclaredMethod("getRepository", String.class);
            method.setAccessible(true);
            return (PanacheRepositoryBase<? extends Vehicles, UUID>) method.invoke(vehicleService, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper to set private fields via reflection
    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = VehicleService.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
