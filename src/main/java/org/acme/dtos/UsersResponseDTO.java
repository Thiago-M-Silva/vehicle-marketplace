package org.acme.dtos;

import java.time.LocalDate;
import java.util.UUID;

import org.acme.enums.EUserRole;
import org.acme.model.Users;

//TODO: use mapstruct here
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
        LocalDate birthDate,
        EUserRole userRole
) {

    public UsersResponseDTO(Users user){
        this(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            user.getPhoneNumber(),
            user.getAddress(),
            user.getCity(),
            user.getState(),
            user.getCountry(),
            user.getCpf(),
            user.getRg(),
            user.getBirthDate(),
            user.getUserRole()
        );
    }
}
