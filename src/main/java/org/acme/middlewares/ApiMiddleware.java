package org.acme.middlewares;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.acme.abstracts.Vehicles;
import org.acme.dtos.BikesRequestDTO;
import org.acme.dtos.BoatsRequestDTO;
import org.acme.dtos.CarsRequestDTO;
import org.acme.dtos.PlanesRequestDTO;
import org.acme.model.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.acme.interfaces.VehicleMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;

@ApplicationScoped
public class ApiMiddleware {

    @Inject VehicleMapper mapper;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converte um DTO para a entidade correspondente
     */
    public Vehicles manageVehiclesTypeRequestDTO(String vehicleType, JsonObject body) {
        if (body == null) {
            throw new IllegalArgumentException("Request body cannot be null");
        }

        try {
            return switch (vehicleType.toLowerCase()) {
                case "cars" -> {
                    CarsRequestDTO dto = objectMapper.readValue(body.toString(), CarsRequestDTO.class);
                    yield mapper.toCars(dto);
                }
                case "bikes" -> {
                    BikesRequestDTO dto = objectMapper.readValue(body.toString(), BikesRequestDTO.class);
                    yield mapper.toBikes(dto);
                }
                case "boats" -> {
                    BoatsRequestDTO dto = objectMapper.readValue(body.toString(), BoatsRequestDTO.class);
                    yield mapper.toBoats(dto);
                }
                case "planes" -> {
                    PlanesRequestDTO dto = objectMapper.readValue(body.toString(), PlanesRequestDTO.class);
                    yield mapper.toPlanes(dto);
                }
                default -> throw new IllegalArgumentException("Invalid vehicle type: " + vehicleType);
            };
        } catch (Exception e) {
            throw new RuntimeException("Error parsing request JSON: " + e.getMessage(), e);
        }
    }

    public List<Vehicles> manageVehiclesTypeRequestDTO(String vehicleType, List<JsonObject> vehicles) {
        if (vehicles == null) {
            throw new IllegalArgumentException("Veículo não pode ser nulo");
        }

        return vehicles.stream()
            .map(vehicle -> manageVehiclesTypeRequestDTO(vehicleType, vehicle))
            .collect(Collectors.toList());
    }

    public List<Vehicles> manageListVehiclesTypeRequestDTO(String vehicleType, List<Map<String, Object>> vehicles) {
        if (vehicles == null) {
            throw new IllegalArgumentException("Veículo não pode ser nulo");
        }

        return vehicles.stream()
            .map(map -> {
                try {
                    // converte Map -> JSON string -> jakarta.json.JsonObject e reaproveita o método existente
                    String json = objectMapper.writeValueAsString(map);
                    jakarta.json.JsonObject jsonObject = jakarta.json.Json.createReader(new java.io.StringReader(json)).readObject();
                    return manageVehiclesTypeRequestDTO(vehicleType, jsonObject);
                } catch (Exception e) {
                    throw new RuntimeException("Error parsing vehicle item: " + e.getMessage(), e);
                }
            })
            .collect(Collectors.toList());
    }

    /**
     * Converte uma entidade única para o DTO de resposta correspondente
     */
    // Para lista de veículos
    public Object manageVehicleTypeResponseDTO(String vehicleType, List<? extends Vehicles> vehicles) {
        if (vehicles == null) {
            throw new IllegalArgumentException("A lista de veículos não pode ser nula");
        }

        return switch (vehicleType.toLowerCase()) {
            case "bikes"  -> mapper.toBikesDTOList((List<Bikes>) vehicles);
            case "cars"   -> mapper.toCarsDTOList((List<Cars>) vehicles);
            case "boats"  -> mapper.toBoatsDTOList((List<Boats>) vehicles);
            case "planes" -> mapper.toPlanesDTOList((List<Planes>) vehicles);
            default -> throw new IllegalArgumentException("Tipo de veículo inválido: " + vehicleType);
        };
    }

    // Para veículo único
    public Object manageVehicleTypeResponseDTO(String vehicleType, Vehicles vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("O veículo não pode ser nulo");
        }

        return switch (vehicleType.toLowerCase()) {
            case "bikes"  -> mapper.toBikesDTO((Bikes) vehicle);
            case "cars"   -> mapper.toCarsDTO((Cars) vehicle);
            case "boats"  -> mapper.toBoatsDTO((Boats) vehicle);
            case "planes" -> mapper.toPlanesDTO((Planes) vehicle);
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