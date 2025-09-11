package org.acme.dtos;

import java.time.LocalDate;
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
    BikesResponseDTO bike,
    BoatsResponseDTO boat,
    CarsRequestDTO cars,
    PlanesResponseDTO plane,
    LocalDate birthDate,
    EUserRole userRole
) {

}
