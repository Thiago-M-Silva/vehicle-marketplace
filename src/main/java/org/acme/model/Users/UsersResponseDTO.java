package org.acme.model.Users;

import java.util.Date;

public record UsersResponseDTO(
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
        Date createDate,
        Date birthDate,
        Date updateDate,
        String userType
) {

    public UsersResponseDTO(Users user){
        this(
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
            user.getCreateDate(),
            user.getBirthDate(),
            user.getUpdateDate(),
            user.getUserType().toString()
        );
    }
}
