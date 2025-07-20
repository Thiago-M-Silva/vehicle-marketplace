package org.acme.dtos;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import org.acme.model.Users;

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
        Date createDate,
        Date updateDate,
        String userType
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
            user.getCreateDate(),
            user.getUpdateDate(),
            user.getUserType().toString()
        );
    }
}
