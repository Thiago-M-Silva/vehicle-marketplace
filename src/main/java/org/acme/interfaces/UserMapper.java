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
import org.mapstruct.Mappings;

@Mapper(componentModel = "cdi")
public interface UserMapper {
   @Mappings({
        @Mapping(target = "bikes", ignore = true),
        @Mapping(target = "boats", ignore = true),
        @Mapping(target = "planes", ignore = true)
    })
    Users toUser(UsersRequestDTO dto);

    @Mappings({
        @Mapping(target = "bike", ignore = true),
        @Mapping(target = "boat", ignore = true),
        @Mapping(target = "plane", ignore = true)
    })
    UsersResponseDTO toUserDTO(Users user);
    List<UsersResponseDTO> toUserDTOList(List<Users> users);

    // for partial updates
    void updateUserFromDTO(UsersRequestDTO dto, @MappingTarget Users entity);

    default LocalDateTime map(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    default Instant map(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}
