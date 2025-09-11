package org.acme.dtos;

import java.time.LocalDate;
import java.util.UUID;

import org.acme.enums.EUserRole;

public record UsersRequestDTO(
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
    BikesRequestDTO bike,
    BoatsRequestDTO boat,
    CarsRequestDTO cars,
    PlanesRequestDTO plane,
    LocalDate birthDate,
    EUserRole userRole
) {

}
