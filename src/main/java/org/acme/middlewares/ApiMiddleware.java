package org.acme.middlewares;

import java.util.List;
import java.util.stream.Collectors;

import org.acme.abstracts.Vehicles;
import org.acme.model.Bikes;
import org.acme.model.Cars;
import org.acme.model.Boats;
import org.acme.model.Planes;
import org.acme.dtos.BikesResponseDTO;
import org.acme.dtos.CarsResponseDTO;
import org.acme.dtos.BoatsResponseDTO;
import org.acme.dtos.PlanesResponseDTO;


import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ApiMiddleware {

    public Vehicles manageVehicleTypeRequestDTO(String vehicleType, Vehicles vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Veículo não pode ser nulo");
        }

        return switch (vehicleType.toLowerCase()) {
            case "bikes" -> convertRequestToEntity((Bikes) vehicle);
            case "cars" -> convertRequestToEntity((Cars) vehicle);
            case "boats" -> convertRequestToEntity((Boats) vehicle);
            case "planes" -> convertRequestToEntity((Planes) vehicle);
            default -> throw new IllegalArgumentException("Tipo de veículo inválido: " + vehicleType);
        };
    }
    
    public List<?> manageVehicleTypeResponseDTO(String vehicleType, List<Vehicles> vehicles) {
        if (vehicles == null || vehicles.isEmpty()) {
            return List.of();
        }

        return switch (vehicleType.toLowerCase()) {
            case "bikes" -> vehicles.stream()
                .filter(Bikes.class::isInstance)
                .map(Bikes.class::cast)
                .map(this::convertEntityToResponse)
                .collect(Collectors.toList());
            case "cars" -> vehicles.stream()
                .filter(Cars.class::isInstance)
                .map(Cars.class::cast)
                .map(this::convertEntityToResponse)
                .collect(Collectors.toList());
            case "boats" -> vehicles.stream()
                .filter(Boats.class::isInstance)
                .map(Boats.class::cast)
                .map(this::convertEntityToResponse)
                .collect(Collectors.toList());
            case "planes" -> vehicles.stream()
                .filter(Planes.class::isInstance)
                .map(Planes.class::cast)
                .map(this::convertEntityToResponse)
                .collect(Collectors.toList());
            default -> throw new IllegalArgumentException("Tipo de veículo inválido: " + vehicleType);
        };
    }

    // Conversores de Bikes
    private Bikes convertRequestToEntity(Bikes bike) {
        if (bike == null) return null;
        var entity = new Bikes();
        entity.setName(bike.getName());
        entity.setBrand(bike.getBrand());
        entity.setYear(bike.getYear());
        entity.setPrice(bike.getPrice());
        entity.setStatus(bike.getStatus());
        entity.setCategory(bike.getCategory());
        entity.setColor(bike.getColor());
        entity.setFuelType(bike.getFuelType());
        return entity;
    }

    private BikesResponseDTO convertEntityToResponse(Bikes bike) {
        if (bike == null) return null;
        return new BikesResponseDTO(
            bike.getName(),
            bike.getBrand(),
            bike.getYear()
        );
    }

    // Conversores de Cars
    private Cars convertRequestToEntity(Cars car) {
        if (car == null) return null;
        var entity = new Cars();
        entity.setName(car.getName());
        entity.setBrand(car.getBrand());
        entity.setYear(car.getYear());
        entity.setPrice(car.getPrice());
        entity.setStatus(car.getStatus());
        entity.setCategory(car.getCategory());
        entity.setColor(car.getColor());
        entity.setFuelType(car.getFuelType());
        return entity;
    }

    private CarsResponseDTO convertEntityToResponse(Cars car) {
        if (car == null) return null;
        return new CarsResponseDTO(
            car.getName(),
            car.getBrand(),
            car.getYear()
        );
    }

    // Conversores de Boats
    private Boats convertRequestToEntity(Boats boat) {
        if (boat == null) return null;
        var entity = new Boats();
        entity.setName(boat.getName());
        entity.setBrand(boat.getBrand());
        entity.setYear(boat.getYear());
        entity.setPrice(boat.getPrice());
        entity.setStatus(boat.getStatus());
        entity.setCategory(boat.getCategory());
        entity.setColor(boat.getColor());
        entity.setFuelType(boat.getFuelType());
        return entity;
    }

    private BoatsResponseDTO convertEntityToResponse(Boats boat) {
        if (boat == null) return null;
        return new BoatsResponseDTO(
            boat.getName(),
            boat.getBrand(),
            boat.getYear()
        );
    }

    // Conversores de Planes
    private Planes convertRequestToEntity(Planes plane) {
        if (plane == null) return null;
        var entity = new Planes();
        entity.setName(plane.getName());
        entity.setBrand(plane.getBrand());
        entity.setYear(plane.getYear());
        entity.setPrice(plane.getPrice());
        entity.setStatus(plane.getStatus());
        entity.setCategory(plane.getCategory());
        entity.setColor(plane.getColor());
        entity.setFuelType(plane.getFuelType());
        return entity;
    }

    private PlanesResponseDTO convertEntityToResponse(Planes plane) {
        if (plane == null) return null;
        return new PlanesResponseDTO(
            plane.getName(),
            plane.getBrand(),
            plane.getYear()
        );
    }
}
