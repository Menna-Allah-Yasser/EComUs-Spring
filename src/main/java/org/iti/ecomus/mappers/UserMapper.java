package org.iti.ecomus.mappers;

import org.iti.ecomus.dto.UserDTO;
import org.iti.ecomus.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDTO(User user);
    User toUser(UserDTO userDTO);
}
