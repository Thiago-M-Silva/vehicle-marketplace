package org.acme.interfaces;

import java.util.List;

import org.acme.abstracts.Vehicles;
import org.acme.dtos.BikesRequestDTO;
import org.acme.dtos.BikesResponseDTO;
import org.acme.dtos.BoatsRequestDTO;
import org.acme.dtos.BoatsResponseDTO;
import org.acme.dtos.CarsRequestDTO;
import org.acme.dtos.CarsResponseDTO;
import org.acme.dtos.PlanesRequestDTO;
import org.acme.dtos.PlanesResponseDTO;
import org.acme.model.Bikes;
import org.acme.model.Boats;
import org.acme.model.Cars;
import org.acme.model.Planes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;

/**
 * VehicleMapper is a MapStruct mapper interface responsible for converting between
 * vehicle entity classes (Cars, Bikes, Boats, Planes) and their corresponding DTOs.
 * <p>
 * This interface defines mapping methods for:
 * <ul>
 *   <li>Converting request DTOs to entity objects (ignoring the "owner" field).</li>
 *   <li>Converting entity objects to response DTOs.</li>
 *   <li>Converting lists of entities to lists of DTOs and vice versa.</li>
 *   <li>Updating existing vehicle entities from request DTOs.</li>
 *   <li>Custom mapping between {@link BigDecimal} and {@code float} types.</li>
 * </ul>
 * <p>
 * The mapper uses the {@code jakarta-cdi} component model for dependency injection
 * and delegates user-related mappings to {@link UserMapper}.
 */
@Mapper(componentModel = "jakarta-cdi", uses = {UserMapper.class})
public interface VehicleMapper {

    // Cars
    @Mapping(target = "owner", ignore = true)
    Cars toCars(CarsRequestDTO dto);
    CarsResponseDTO toCarsDTO(Cars car);
    List<CarsResponseDTO> toCarsDTOList(List<Cars> cars);
    CarsRequestDTO toCarsRequestDTO(Cars car);
    List<CarsRequestDTO> toCarsRequestDTO(List<Cars> cars);
    void updateCarsFromDTO(CarsRequestDTO dto, @MappingTarget Vehicles entity);

    // Bikes
    @Mapping(target = "owner", ignore = true)
    Bikes toBikes(BikesRequestDTO dto);
    BikesResponseDTO toBikesDTO(Bikes bike);
    List<BikesResponseDTO> toBikesDTOList(List<Bikes> bikes);
    BikesRequestDTO toBikesRequestDTO(Bikes vehicle);
    List<BikesRequestDTO> toBikesRequestDTO(List<Bikes> bikes);
    void updateBikesFromDTO(BikesRequestDTO dto, @MappingTarget Vehicles entity);

    // Boats
    @Mapping(target = "owner", ignore = true)
    Boats toBoats(BoatsRequestDTO dto);
    BoatsResponseDTO toBoatsDTO(Boats boat);
    List<BoatsResponseDTO> toBoatsDTOList(List<Boats> boats);
    BoatsRequestDTO toBoatsRequestDTO(Boats boat);
    List<BoatsRequestDTO> toBoatsRequestDTO(List<Boats> boats);
    void updateBoatsFromDTO(BoatsRequestDTO dto, @MappingTarget Vehicles entity);

    // Planes
    @Mapping(target = "owner", ignore = true)
    Planes toPlanes(PlanesRequestDTO dto);
    PlanesResponseDTO toPlanesDTO(Planes plane);
    List<PlanesResponseDTO> toPlanesDTOList(List<Planes> planes);
    PlanesRequestDTO toPlanesRequestDTO(Planes plane);
    List<PlanesRequestDTO> toPlanesRequestDTO(List<Planes> planes);
    void updatePlanesFromDTO(PlanesRequestDTO dto, @MappingTarget Vehicles entity);

    // BigDecimal ↔ float conversions
    default float map(BigDecimal value) {
        return value == null ? 0f : value.floatValue();
    }

    default BigDecimal map(float value) {
        return BigDecimal.valueOf(value);
    }
}