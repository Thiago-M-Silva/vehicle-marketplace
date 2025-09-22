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

@Mapper(componentModel = "jakarta-cdi")
public interface UserMapper {

    // DTO → Entity
    @Mapping(target = "transaction", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Users toUser(UsersRequestDTO dto);

    // Entity → DTO
    UsersResponseDTO toUserDTO(Users user);

    // List<Entity> → List<DTO>
    List<UsersResponseDTO> toUserDTOList(List<Users> users);

    // Partial update (keeps entity fields not present in DTO)
    @Mapping(target = "transaction", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    void updateUserFromDTO(UsersRequestDTO dto, @MappingTarget Users entity);

    // Type conversions for Instant ↔ LocalDate
    default LocalDate map(Instant instant) {
        return instant == null ? null : instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    default Instant map(LocalDate localDate) {
        return localDate == null ? null : localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }
}