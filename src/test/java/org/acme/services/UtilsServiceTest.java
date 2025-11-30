package org.acme.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.acme.model.Bikes;
import org.acme.model.Boats;
import org.acme.model.Cars;
import org.acme.model.Planes;
import org.acme.model.Users;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class UtilsServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private UtilsService utilsService;

    private Users seller;
    private Users buyer;
    private UUID vehicleId;

    @BeforeEach
    void setUp() {
        seller = new Users();
        seller.setBikes(new ArrayList<>());
        seller.setBoats(new ArrayList<>());
        seller.setCars(new ArrayList<>());
        seller.setPlanes(new ArrayList<>());

        buyer = new Users();
        buyer.setBikes(new ArrayList<>());
        buyer.setBoats(new ArrayList<>());
        buyer.setCars(new ArrayList<>());
        buyer.setPlanes(new ArrayList<>());

        vehicleId = UUID.randomUUID();
    }

    @Test
    void testUpdateOwnerWithNullSellerStripeId() {
        assertThrows(IllegalArgumentException.class,
                () -> utilsService.updateOwner(null, "buyer@email.com", vehicleId, "bikes"));
    }

    @Test
    void testUpdateOwnerWithNullBuyerEmail() {
        assertThrows(IllegalArgumentException.class,
                () -> utilsService.updateOwner("stripe_123", null, vehicleId, "bikes"));
    }

    @Test
    void testUpdateOwnerWithNullVehicleId() {
        assertThrows(IllegalArgumentException.class,
                () -> utilsService.updateOwner("stripe_123", "buyer@email.com", null, "bikes"));
    }

    @Test
    void testUpdateOwnerWithInvalidVehicleType() {
        Bikes bike = new Bikes();
        bike.setStorage(0);

        when(userService.searchUsers(any())).thenReturn(List.of(seller), List.of(buyer));
        when(vehicleService.findById("invalid", vehicleId)).thenReturn(bike);

        assertThrows(IllegalArgumentException.class,
                () -> utilsService.updateOwner("stripe_123", "buyer@email.com", vehicleId, "invalid"));
    }

    @Test
    void testUpdateOwnerBikes() {
        Bikes bike = new Bikes();
        bike.setStorage(0);
        seller.getBikes().add(bike);

        when(userService.searchUsers(any())).thenReturn(List.of(seller), List.of(buyer));
        when(vehicleService.findById("bikes", vehicleId)).thenReturn(bike);

        utilsService.updateOwner("stripe_123", "buyer@email.com", vehicleId, "bikes");

        assertFalse(seller.getBikes().contains(bike));
        assertTrue(buyer.getBikes().contains(bike));
        assertEquals(1, bike.getStorage());
        assertEquals(buyer, bike.getOwner());
    }

    @Test
    void testUpdateOwnerBoats() {
        Boats boat = new Boats();
        boat.setStorage(0);
        seller.getBoats().add(boat);

        when(userService.searchUsers(any())).thenReturn(List.of(seller), List.of(buyer));
        when(vehicleService.findById("boats", vehicleId)).thenReturn(boat);

        utilsService.updateOwner("stripe_123", "buyer@email.com", vehicleId, "boats");

        assertFalse(seller.getBoats().contains(boat));
        assertTrue(buyer.getBoats().contains(boat));
        assertEquals(1, boat.getStorage());
        assertEquals(buyer, boat.getOwner());
    }

    @Test
    void testUpdateOwnerCars() {
        Cars car = new Cars();
        car.setStorage(0);
        seller.getCars().add(car);

        when(userService.searchUsers(any())).thenReturn(List.of(seller), List.of(buyer));
        when(vehicleService.findById("cars", vehicleId)).thenReturn(car);

        utilsService.updateOwner("stripe_123", "buyer@email.com", vehicleId, "cars");

        assertFalse(seller.getCars().contains(car));
        assertTrue(buyer.getCars().contains(car));
        assertEquals(1, car.getStorage());
        assertEquals(buyer, car.getOwner());
    }

    @Test
    void testUpdateOwnerPlanes() {
        Planes plane = new Planes();
        plane.setStorage(0);
        seller.getPlanes().add(plane);

        when(userService.searchUsers(any())).thenReturn(List.of(seller), List.of(buyer));
        when(vehicleService.findById("planes", vehicleId)).thenReturn(plane);

        utilsService.updateOwner("stripe_123", "buyer@email.com", vehicleId, "planes");

        assertFalse(seller.getPlanes().contains(plane));
        assertTrue(buyer.getPlanes().contains(plane));
        assertEquals(1, plane.getStorage());
        assertEquals(buyer, plane.getOwner());
    }

    @Test
    void testUpdateOwnerWithStorageFlagNotZero() {
        Bikes bike = new Bikes();
        bike.setStorage(1);
        seller.getBikes().add(bike);

        when(userService.searchUsers(any())).thenReturn(List.of(seller), List.of(buyer));
        when(vehicleService.findById("bikes", vehicleId)).thenReturn(bike);

        utilsService.updateOwner("stripe_123", "buyer@email.com", vehicleId, "bikes");

        assertTrue(seller.getBikes().contains(bike));
        assertFalse(buyer.getBikes().contains(bike));
    }

    @Test
    void testUpdateOwnerCaseInsensitiveVehicleType() {
        Cars car = new Cars();
        car.setStorage(0);
        seller.getCars().add(car);

        when(userService.searchUsers(any())).thenReturn(List.of(seller), List.of(buyer));
        when(vehicleService.findById("CARS", vehicleId)).thenReturn(car);

        utilsService.updateOwner("stripe_123", "buyer@email.com", vehicleId, "CARS");

        assertFalse(seller.getCars().contains(car));
        assertTrue(buyer.getCars().contains(car));
    }
}
