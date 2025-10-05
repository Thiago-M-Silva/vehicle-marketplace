package org.acme.dtos;

import java.time.LocalDate;
import java.util.List;
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
    String stripeAccountId,
    List<BikesRequestDTO> bikes,
    List<BoatsRequestDTO> boats,
    List<CarsRequestDTO> cars,
    List<PlanesRequestDTO> planes,
    LocalDate birthDate,
    EUserRole userRole
) {}
