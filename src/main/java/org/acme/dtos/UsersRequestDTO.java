package org.acme.dtos;

import java.time.LocalDate;
import java.util.Date;

import org.acme.enums.EUserRole;

public record UsersRequestDTO(
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
    EUserRole userType
) {

}
