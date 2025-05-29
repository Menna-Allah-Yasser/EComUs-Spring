package org.iti.ecomus.config.security;

import org.iti.ecomus.entity.User;
import org.iti.ecomus.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;

@Service
public class UserManager implements UserDetailsManager {

    @Autowired
    UserRepo userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserDetails userDetails) {

        User user = (User) userDetails;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        User user = (User) userDetails;
        Optional<User> existingUserOpt = userRepository.findByUserName(user.getUsername());
        if (existingUserOpt.isEmpty()) {
            throw new UsernameNotFoundException("Cannot update non-existing user: " + user.getUsername());
        }
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String username) {
        Optional<User> userOpt = userRepository.findByUserName(username);
        userOpt.ifPresent(userRepository::delete);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        UserDetails currentUser = SecurityContextHolder
                .getContext().getAuthentication().getPrincipal() instanceof UserDetails
                ? (UserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()
                : null;

        if (currentUser == null) {
            throw new IllegalStateException("No authenticated user found to change password.");
        }

        User user = userRepository.findByUserName(currentUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password does not match.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean userExists(String username) {

        return userRepository.findByUserName(username).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch the user from the repository by username
        Optional<User> userOptional = userRepository.findByUserName(username);

        // Check if the user exists
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(MessageFormat.format("User with username {0} not found", username));
        }

        // Return the UserDetails extracted from the User entity
        return userOptional.get();
    }

}