package org.acme.middlewares;

import io.quarkus.test.junit.QuarkusTest;

import java.math.BigDecimal;
import java.util.List;

import org.acme.abstracts.Vehicles;
import org.acme.model.Bikes;
import org.acme.model.Cars;
import org.acme.model.Boats;
import org.acme.model.Planes;

import org.acme.dtos.BikesResponseDTO;
import org.acme.dtos.CarsResponseDTO;
import org.acme.dtos.BoatsResponseDTO;
import org.acme.dtos.PlanesResponseDTO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.acme.enums.*;

@QuarkusTest
public class ApiMiddlewareTest {

    private final ApiMiddleware apiMiddleware = new ApiMiddleware();

    @Test
    void testManageVehicleTypeRequestDTO_Bikes() {
        Bikes bike = new Bikes();
        bike.setName("Bike1");
        bike.setBrand("BrandA");
        bike.setYear(2020);
        bike.setPrice(new BigDecimal("1000.0"));
        bike.setBikeStatus(EStatus.NEW);
        bike.setCategory(ECategory.COMPACT_CAR);
        bike.setColor(EColors.RED);
        bike.setFuelType(EFuelType.ALCOHOL);

        Vehicles result = apiMiddleware.manageVehicleTypeRequestDTO("bikes", bike);

        Assertions.assertTrue(result instanceof Bikes);
        Bikes resultBike = (Bikes) result;
        Assertions.assertEquals("Bike1", resultBike.getName());
        Assertions.assertEquals("BrandA", resultBike.getBrand());
        Assertions.assertEquals(2020, resultBike.getYear());
        Assertions.assertEquals(new BigDecimal("1000.0"), resultBike.getPrice());
        Assertions.assertEquals("available", resultBike.getBikeStatus());
        Assertions.assertEquals("mountain", resultBike.getCategory());
        Assertions.assertEquals("red", resultBike.getColor());
        Assertions.assertEquals("none", resultBike.getFuelType());
    }

    @Test
    void testManageVehicleTypeRequestDTO_Cars() {
        Cars car = new Cars();
        car.setName("Car1");
        car.setBrand("BrandB");
        car.setYear(2019);
        car.setPrice(new BigDecimal("20000.0"));
        car.setCarStatus(EStatus.NEW);
        car.setCategory(ECategory.COMPACT_CAR);
        car.setColor(EColors.RED);
        car.setFuelType(EFuelType.ALCOHOL);

        Vehicles result = apiMiddleware.manageVehicleTypeRequestDTO("cars", car);

        Assertions.assertTrue(result instanceof Cars);
        Cars resultCar = (Cars) result;
        Assertions.assertEquals("Car1", resultCar.getName());
        Assertions.assertEquals("BrandB", resultCar.getBrand());
        Assertions.assertEquals(2019, resultCar.getYear());
        Assertions.assertEquals(new BigDecimal("20000.0"), resultCar.getPrice());
        Assertions.assertEquals("sold", resultCar.getCarStatus());
        Assertions.assertEquals("sedan", resultCar.getCategory());
        Assertions.assertEquals("blue", resultCar.getColor());
        Assertions.assertEquals("gasoline", resultCar.getFuelType());
    }

    @Test
    void testManageVehicleTypeRequestDTO_Boats() {
        Boats boat = new Boats();
        boat.setName("Boat1");
        boat.setBrand("BrandC");
        boat.setYear(2018);
        boat.setPrice(new BigDecimal("50000.0"));
        boat.setBoatStatus(EStatus.NEW);
        boat.setCategory(ECategory.COMPACT_CAR);
        boat.setColor(EColors.RED);
        boat.setFuelType(EFuelType.ALCOHOL);

        Vehicles result = apiMiddleware.manageVehicleTypeRequestDTO("boats", boat);

        Assertions.assertTrue(result instanceof Boats);
        Boats resultBoat = (Boats) result;
        Assertions.assertEquals("Boat1", resultBoat.getName());
        Assertions.assertEquals("BrandC", resultBoat.getBrand());
        Assertions.assertEquals(2018, resultBoat.getYear());
        Assertions.assertEquals(new BigDecimal("50000.0"), resultBoat.getPrice());
        Assertions.assertEquals("available", resultBoat.getBoatStatus());
        Assertions.assertEquals("yacht", resultBoat.getCategory());
        Assertions.assertEquals("white", resultBoat.getColor());
        Assertions.assertEquals("diesel", resultBoat.getFuelType());
    }

    @Test
    void testManageVehicleTypeRequestDTO_Planes() {
        Planes plane = new Planes();
        plane.setName("Plane1");
        plane.setBrand("BrandD");
        plane.setYear(2015);
        plane.setPrice(new BigDecimal("1000000.0"));
        plane.setPlaneStatus(EStatus.NEW);
        plane.setCategory(ECategory.COMPACT_CAR);
        plane.setColor(EColors.RED);
        plane.setFuelType(EFuelType.ALCOHOL);

        Vehicles result = apiMiddleware.manageVehicleTypeRequestDTO("planes", plane);

        Assertions.assertTrue(result instanceof Planes);
        Planes resultPlane = (Planes) result;
        Assertions.assertEquals("Plane1", resultPlane.getName());
        Assertions.assertEquals("BrandD", resultPlane.getBrand());
        Assertions.assertEquals(2015, resultPlane.getYear());
        Assertions.assertEquals(new BigDecimal("1000000.0"), resultPlane.getPrice());
        Assertions.assertEquals("maintenance", resultPlane.getPlaneStatus());
        Assertions.assertEquals("private", resultPlane.getCategory());
        Assertions.assertEquals("silver", resultPlane.getColor());
        Assertions.assertEquals("jet fuel", resultPlane.getFuelType());
    }

    @Test
    void testManageVehicleTypeRequestDTO_NullVehicle_ThrowsException() {
        IllegalArgumentException ex = Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> apiMiddleware.manageVehicleTypeRequestDTO("bikes", null)
        );
        Assertions.assertEquals("Veículo não pode ser nulo", ex.getMessage());
    }

    @Test
    void testManageVehicleTypeRequestDTO_InvalidType_ThrowsException() {
        Bikes bike = new Bikes();
        IllegalArgumentException ex = Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> apiMiddleware.manageVehicleTypeRequestDTO("skates", bike)
        );
        Assertions.assertTrue(ex.getMessage().contains("Tipo de veículo inválido"));
    }

    @Test
    void testManageVehicleTypeResponseDTO_Bikes() {
        Bikes bike1 = new Bikes();
        bike1.setName("Bike1");
        bike1.setBrand("BrandA");
        bike1.setYear(2020);

        Bikes bike2 = new Bikes();
        bike2.setName("Bike2");
        bike2.setBrand("BrandB");
        bike2.setYear(2021);

        var list = java.util.List.of(bike1, bike2);
        var result = apiMiddleware.manageVehicleTypeResponseDTO("bikes", new java.util.ArrayList<>(list));

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.get(0) instanceof BikesResponseDTO);
        BikesResponseDTO dto1 = (BikesResponseDTO) result.get(0);
        Assertions.assertEquals("Bike1", dto1.name());
        Assertions.assertEquals("BrandA", dto1.brand());
        Assertions.assertEquals(2020, dto1.year());
    }

    @Test
    void testManageVehicleTypeResponseDTO_Cars() {
        Cars car1 = new Cars();
        car1.setName("Car1");
        car1.setBrand("BrandA");
        car1.setYear(2018);

        Cars car2 = new Cars();
        car2.setName("Car2");
        car2.setBrand("BrandB");
        car2.setYear(2019);

        var list = java.util.List.of(car1, car2);
        var result = apiMiddleware.manageVehicleTypeResponseDTO("cars", new java.util.ArrayList<>(list));

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.get(1) instanceof CarsResponseDTO);
        CarsResponseDTO dto2 = (CarsResponseDTO) result.get(1);
        Assertions.assertEquals("Car2", dto2.name());
        Assertions.assertEquals("BrandB", dto2.brand());
        Assertions.assertEquals(2019, dto2.year());
    }

    @Test
    void testManageVehicleTypeResponseDTO_Boats() {
        Boats boat = new Boats();
        boat.setName("Boat1");
        boat.setBrand("BrandC");
        boat.setYear(2015);

        var list = java.util.List.of(boat);
        var result = apiMiddleware.manageVehicleTypeResponseDTO("boats", new java.util.ArrayList<>(list));

        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.get(0) instanceof BoatsResponseDTO);
        BoatsResponseDTO dto = (BoatsResponseDTO) result.get(0);
        Assertions.assertEquals("Boat1", dto.name());
        Assertions.assertEquals("BrandC", dto.brand());
        Assertions.assertEquals(2015, dto.year());
    }

    @Test
    void testManageVehicleTypeResponseDTO_Planes() {
        Planes plane = new Planes();
        plane.setName("Plane1");
        plane.setBrand("BrandD");
        plane.setYear(2010);

        var list = java.util.List.of(plane);
        var result = apiMiddleware.manageVehicleTypeResponseDTO("planes", new java.util.ArrayList<>(list));

        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.get(0) instanceof PlanesResponseDTO);
        PlanesResponseDTO dto = (PlanesResponseDTO) result.get(0);
        Assertions.assertEquals("Plane1", dto.name());
        Assertions.assertEquals("BrandD", dto.brand());
        Assertions.assertEquals(2010, dto.year());
    }

    @Test
    void testManageVehicleTypeResponseDTO_EmptyList() {
        var result = apiMiddleware.manageVehicleTypeResponseDTO("bikes", java.util.Collections.emptyList());
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testManageVehicleTypeResponseDTO_NullList() {
        var result = apiMiddleware.manageVehicleTypeResponseDTO("cars", null);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testManageVehicleTypeResponseDTO_InvalidType_ThrowsException() {
        Bikes bike = new Bikes();
        List<Vehicles> list = java.util.List.of(bike);
        IllegalArgumentException ex = Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> apiMiddleware.manageVehicleTypeResponseDTO("skates", list)
        );
        Assertions.assertTrue(ex.getMessage().contains("Tipo de veículo inválido"));
    }

    @Test
    void testManageVehicleTypeResponseDTO_MixedTypes_FilteredCorrectly() {
        Bikes bike = new Bikes();
        bike.setName("Bike1");
        bike.setBrand("BrandA");
        bike.setYear(2020);

        Cars car = new Cars();
        car.setName("Car1");
        car.setBrand("BrandB");
        car.setYear(2019);

        var list = java.util.List.of(bike, car);
        var result = apiMiddleware.manageVehicleTypeResponseDTO("bikes", new java.util.ArrayList<>(list));

        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.get(0) instanceof BikesResponseDTO);
        BikesResponseDTO dto = (BikesResponseDTO) result.get(0);
        Assertions.assertEquals("Bike1", dto.name());
    }
}
