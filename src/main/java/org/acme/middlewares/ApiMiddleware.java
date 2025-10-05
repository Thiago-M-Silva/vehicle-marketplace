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
    
    final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Processes a vehicle request based on the specified vehicle type and request body.
     * 
     * This method deserializes the provided JSON body into the appropriate request DTO
     * according to the vehicle type ("cars", "bikes", "boats", or "planes"), then maps
     * it to a corresponding {@link Vehicles} entity using the mapper.
     *
     * @param vehicleType the type of vehicle ("cars", "bikes", "boats", or "planes")
     * @param body the JSON request body containing vehicle data
     * @return a {@link Vehicles} entity mapped from the request DTO
     * @throws IllegalArgumentException if the request body is null or the vehicle type is invalid
     * @throws RuntimeException if an error occurs during JSON parsing or mapping
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

    /**
     * Processes a list of vehicle data represented as {@link JsonObject} and converts each entry
     * to a {@link Vehicles} object based on the specified vehicle type.
     *
     * @param vehicleType the type of vehicles to be managed (e.g., "car", "truck", etc.)
     * @param vehicles a list of {@link JsonObject} representing vehicle data; must not be {@code null}
     * @return a list of {@link Vehicles} objects corresponding to the input data and vehicle type
     * @throws IllegalArgumentException if the {@code vehicles} list is {@code null}
     */
    public List<Vehicles> manageVehiclesTypeRequestDTO(String vehicleType, List<JsonObject> vehicles) {
        if (vehicles == null) {
            throw new IllegalArgumentException("Veículo não pode ser nulo");
        }

        return vehicles.stream()
            .map(vehicle -> manageVehiclesTypeRequestDTO(vehicleType, vehicle))
            .collect(Collectors.toList());
    }

    /**
     * Processes a list of vehicle data represented as maps, converting each map to a JSON object
     * and delegating to {@code manageVehiclesTypeRequestDTO} for further handling based on the vehicle type.
     *
     * @param vehicleType the type of vehicle to be processed
     * @param vehicles a list of maps, each representing a vehicle's data
     * @return a list of {@code Vehicles} objects resulting from processing each input map
     * @throws IllegalArgumentException if the {@code vehicles} list is {@code null}
     * @throws RuntimeException if an error occurs during JSON serialization or processing of a vehicle item
     */
    public List<Vehicles> manageListVehiclesTypeRequestDTO(String vehicleType, List<Map<String, Object>> vehicles) {
        if (vehicles == null) {
            throw new IllegalArgumentException("Veículo não pode ser nulo");
        }

        return vehicles.stream()
            .map(map -> {
                try {
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
     * Converts a list of vehicle entities to their corresponding DTO list based on the specified vehicle type.
     *
     * @param vehicleType the type of vehicle ("bikes", "cars", "boats", or "planes")
     * @param vehicles the list of vehicle entities to be converted; must not be null
     * @return a list of DTOs corresponding to the specified vehicle type
     * @throws IllegalArgumentException if the vehicleType is invalid or if vehicles is null
     */
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

    /**
     * Converts a {@link Vehicles} object to its corresponding DTO based on the specified vehicle type.
     *
     * @param vehicleType the type of the vehicle ("bikes", "cars", "boats", or "planes")
     * @param vehicle the vehicle entity to be converted; must not be {@code null}
     * @return the DTO representation of the vehicle, specific to the given type
     * @throws IllegalArgumentException if the vehicle is {@code null} or if the vehicle type is invalid
     */
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
     * Processes a list of {@link Vehicles} and returns a list of corresponding DTOs based on the specified vehicle type.
     *
     * This method filters the input list of vehicles by the provided vehicle type ("bikes", "cars", "boats", or "planes"),
     * casts them to the appropriate subclass, and maps them to their respective DTO representations using the mapper.
     *
     * @param vehicleType the type of vehicle to filter and map ("bikes", "cars", "boats", or "planes")
     * @param vehicles the list of {@link Vehicles} to process
     * @return a list of DTOs corresponding to the specified vehicle type; returns an empty list if the input is null or empty
     * @throws IllegalArgumentException if the provided vehicle type is invalid
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