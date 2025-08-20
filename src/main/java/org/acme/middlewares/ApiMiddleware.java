package org.acme.middlewares;

import java.util.List;
import java.util.stream.Collectors;

import org.acme.abstracts.Vehicles;
import org.acme.model.Bikes;
import org.acme.model.Cars;
import org.acme.model.Boats;
import org.acme.model.Planes;
import org.acme.interfaces.VehicleMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ApiMiddleware {

    @Inject
    VehicleMapper mapper;

    public Vehicles manageVehiclesTypeRequestDTO(String vehicleType, Vehicles vehicle){
        if (vehicle == null) {
            throw new IllegalArgumentException("Veículo não pode ser nulo");
        }

        return switch (vehicleType.toLowerCase()) {
            case "bikes" -> mapper.toBikes((Bikes) vehicle);
            case "cars" -> mapper.toCars((Cars) vehicle);
            case "boats" -> mapper.toBoats((Boats) vehicle);
            case "planes" -> mapper.toPlanes((Planes) vehicle);
            default -> throw new IllegalArgumentException("Tipo de veículo inválido: " + vehicleType);
        };
    }

    public List<?> manageVehicleTypeResponseDTO(String vehicleType, List<Vehicles> vehicles){
        if (vehicles == null || vehicles.isEmpty()){
            return List.of();
        }

        return switch(vehicleType.toLowerCase()){
            case "bikes" -> mapper.toBikesDTOList(
                vehicles.stream()
                    .filter(Bikes.class::isInstance)
                    .map(Bikes.class::cast)
                    .collect(Collectors.toList())
            );
            case "cars" -> mapper.toCarsDTOList(
                vehicles.stream()
                    .filter(Cars.class::isInstance)
                    .map(Cars.class::cast)
                    .collect(Collectors.toList())
            );
            case "boats" -> mapper.toBoatsDTOList(
                vehicles.stream()
                    .filter(Boats.class::isInstance)
                    .map(Boats.class::cast)
                    .collect(Collectors.toList())
            );
            case "planes" -> mapper.toPlanesDTOList(
                vehicles.stream()
                    .filter(Planes.class::isInstance)
                    .map(Planes.class::cast)
                    .collect(Collectors.toList())
            );
            default -> throw new IllegalArgumentException();
        };
    }
}