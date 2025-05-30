package org.iti.ecomus.service;

import org.iti.ecomus.dto.UserDTO;
import org.iti.ecomus.entity.User;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long userId);


}
