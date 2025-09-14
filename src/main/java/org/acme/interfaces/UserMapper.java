package org.acme.interfaces;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.acme.dtos.UsersRequestDTO;
import org.acme.dtos.UsersResponseDTO;
import org.acme.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface UserMapper {

    // DTO → Entity
    @Mapping(target = "bikes", ignore = true)
    @Mapping(target = "boats", ignore = true)
    @Mapping(target = "planes", ignore = true)
    @Mapping(target = "cars", ignore = true)
    @Mapping(target = "transaction", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Users toUser(UsersRequestDTO dto);
    
    // Entity → DTO
    @Mapping(target = "bike", ignore = true)
    @Mapping(target = "boat", ignore = true)
    @Mapping(target = "plane", ignore = true)
    @Mapping(target = "cars", ignore = true)
    UsersResponseDTO toUserDTO(Users user);

    // List<Entity> → List<DTO>
    List<UsersResponseDTO> toUserDTOList(List<Users> users);

    // Partial update (keeps entity fields not present in DTO)
    @Mapping(target = "bikes", ignore = true)
    @Mapping(target = "boats", ignore = true)
    @Mapping(target = "planes", ignore = true)
    @Mapping(target = "cars", ignore = true)
    @Mapping(target = "transaction", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    void updateUserFromDTO(UsersRequestDTO dto, @MappingTarget Users entity);

    // Type conversions
    default LocalDateTime map(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    default Instant map(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}

