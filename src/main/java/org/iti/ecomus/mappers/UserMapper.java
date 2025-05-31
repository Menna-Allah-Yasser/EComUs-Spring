package org.iti.ecomus.mappers;

import org.iti.ecomus.dto.UserDTO;
import org.iti.ecomus.dto.UserSignUpDTO;
import org.iti.ecomus.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDTO(User user);
    User toUser(UserDTO userDTO);
    List<UserDTO> toUserDTOs(List<User> users);

    UserSignUpDTO toUserDTOFromSignUp(User user);
    User toUserFromSignUp(UserSignUpDTO userDTO);
}
