package org.acme.interfaces;

import java.util.List;

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
import org.mapstruct.Mappings;

@Mapper(componentModel = "cdi")
public interface VehicleMapper {
    // Bikes
    @Mappings({
        @Mapping(target = "createDate", ignore = true),
        @Mapping(target = "updateDate", ignore = true)
    })
    Bikes toBikes(BikesRequestDTO dto);
    BikesResponseDTO toBikesDTO(Bikes bike);
    List<BikesResponseDTO> toBikesDTOList(List<Bikes> bikes);

    // Cars
    @Mappings({
        @Mapping(target = "createDate", ignore = true),
        @Mapping(target = "updateDate", ignore = true)
    })
    Cars toCars(CarsRequestDTO dto);
    CarsResponseDTO toCarsDTO(Cars car);
    List<CarsResponseDTO> toCarsDTOList(List<Cars> cars);

    // Boats
    @Mappings({
        @Mapping(target = "createDate", ignore = true),
        @Mapping(target = "updateDate", ignore = true)
    })
    Boats toBoats(BoatsRequestDTO dto);
    BoatsResponseDTO toBoatsDTO(Boats boat);
    List<BoatsResponseDTO> toBoatsDTOList(List<Boats> boats);

    // Planes
    @Mappings({
        @Mapping(target = "createDate", ignore = true),
        @Mapping(target = "updateDate", ignore = true)
    })
    Planes toPlanes(PlanesRequestDTO dto);
    
    PlanesResponseDTO toPlanesDTO(Planes plane);
    List<PlanesResponseDTO> toPlanesDTOList(List<Planes> planes);
}
