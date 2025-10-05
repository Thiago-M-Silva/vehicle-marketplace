package org.acme.interfaces;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.acme.dtos.UsersRequestDTO;
import org.acme.dtos.UsersResponseDTO;
import org.acme.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper interface for converting between Users entities and their corresponding DTOs.
 * Utilizes MapStruct for automatic mapping and Jakarta CDI for dependency injection.
 *
 * Mapping rules:
 * - Ignores the 'transaction', 'createDate', and 'updateDate' fields when mapping from UsersRequestDTO to Users.
 * - Provides methods for mapping single entities and lists.
 * - Includes custom mapping methods for converting between {@link Instant} and {@link LocalDate}.
 *
 * Methods:
 * - {@link #toUser(UsersRequestDTO)}: Maps a UsersRequestDTO to a Users entity.
 * - {@link #toUserDTO(Users)}: Maps a Users entity to a UsersResponseDTO.
 * - {@link #toUserDTOList(List)}: Maps a list of Users entities to a list of UsersResponseDTOs.
 * - {@link #updateUserFromDTO(UsersRequestDTO, Users)}: Updates an existing Users entity from a UsersRequestDTO.
 * - {@link #map(Instant)}: Converts an Instant to a LocalDate.
 * - {@link #map(LocalDate)}: Converts a LocalDate to an Instant.
 */
@Mapper(componentModel = "jakarta-cdi")
public interface UserMapper {

    @Mapping(target = "transaction", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Users toUser(UsersRequestDTO dto);

    UsersResponseDTO toUserDTO(Users user);

    List<UsersResponseDTO> toUserDTOList(List<Users> users);

    @Mapping(target = "transaction", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "keycloakId", ignore = true)
    void updateUserFromDTO(UsersRequestDTO dto, @MappingTarget Users entity);

    default LocalDate map(Instant instant) {
        return instant == null ? null : instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    default Instant map(LocalDate localDate) {
        return localDate == null ? null : localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }
}