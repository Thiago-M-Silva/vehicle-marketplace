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

@Mapper(componentModel = "cdi")
public interface VehicleMapper {

    Bikes toBikes(Bikes vehicle);
    Bikes toBikes(BikesRequestDTO dto);
    BikesResponseDTO toBikesDTO(Bikes bike);
    List<BikesResponseDTO> toBikesDTOList(List<Bikes> bikes);
    
    Cars toCars(Cars vehicle);
    Cars toCars(CarsRequestDTO dto);
    CarsResponseDTO toCarsDTO(Cars cars);
    List<CarsResponseDTO> toCarsDTOList(List<Cars> cars);
    
    Boats toBoats(Boats vehicle);
    Boats toBoats(BoatsRequestDTO dto);
    BoatsResponseDTO toBoatsDTO(Boats boats);
    List<BoatsResponseDTO> toBoatsDTOList(List<Boats> boats);
    
    Planes toPlanes(Planes vehicle);
    Planes toPlanes(PlanesRequestDTO dto);
    PlanesResponseDTO toPlanesDTO(Planes bike);
    List<PlanesResponseDTO> toPlanesDTOList(List<Planes> bikes);
}
