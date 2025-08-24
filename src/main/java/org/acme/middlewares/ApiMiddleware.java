package org.acme.middlewares;

import java.util.List;
import java.util.stream.Collectors;

import org.acme.abstracts.Vehicles;
import org.acme.dtos.*;
import org.acme.model.*;
import org.acme.interfaces.VehicleMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ApiMiddleware {

    @Inject
    VehicleMapper mapper;

    /**
     * Converte um DTO para a entidade correspondente
     */
    public Vehicles manageVehiclesTypeRequestDTO(String vehicleType, Object dto){
        if (dto == null) {
            throw new IllegalArgumentException("DTO não pode ser nulo");
        }

        return switch (vehicleType.toLowerCase()) {
            case "bikes" -> mapper.toBikes((BikesRequestDTO) dto);
            case "cars"  -> mapper.toCars((CarsRequestDTO) dto);
            case "boats" -> mapper.toBoats((BoatsRequestDTO) dto);
            case "planes"-> mapper.toPlanes((PlanesRequestDTO) dto);
            default -> throw new IllegalArgumentException("Tipo de veículo inválido: " + vehicleType);
        };
    }

    /**
     * Converte uma entidade única para o DTO de resposta correspondente
     */
    public Object manageVehicleTypeResponseDTO(String vehicleType, List<Vehicles> vehicles){
        if (vehicles == null) {
            throw new IllegalArgumentException("Veículo não pode ser nulo");
        }

        return switch (vehicleType.toLowerCase()) {
            case "bikes" -> mapper.toBikesDTO((Bikes) vehicles);
            case "cars"  -> mapper.toCarsDTO((Cars) vehicles);
            case "boats" -> mapper.toBoatsDTO((Boats) vehicles);
            case "planes"-> mapper.toPlanesDTO((Planes) vehicles);
            default -> throw new IllegalArgumentException("Tipo de veículo inválido: " + vehicleType);
        };
    }

    /**
     * Converte uma lista de entidades para a lista de DTOs correspondente
     */
    public List<?> manageVehicleTypeResponseDTOList(String vehicleType, List<Vehicles> vehicles){
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
            default -> throw new IllegalArgumentException("Tipo de veículo inválido: " + vehicleType);
        };
    }
}
