package org.acme.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.acme.enums.EUserRole;

public record UsersResponseDTO(
    UUID id,
    String name,
    String email,
    String password,
    String phoneNumber,
    String address,
    String city,
    String state,
    String country,
    String cpf,
    String rg,
    String stripeAccountId,
    List<BikesResponseDTO> bikes,
    List<BoatsResponseDTO> boats,
    List<CarsResponseDTO> cars,
    List<PlanesResponseDTO> planes,
    LocalDate birthDate,
    EUserRole userRole
) {

}
