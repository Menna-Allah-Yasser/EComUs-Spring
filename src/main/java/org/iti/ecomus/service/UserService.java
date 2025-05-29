package org.iti.ecomus.service;

import org.iti.ecomus.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUserById(Long userId);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long userId);

    Long checkValidEmail(String email);

    User getUserByEmail(String email);
}
