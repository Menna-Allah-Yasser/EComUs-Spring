package org.iti.ecomus.config.security;

import org.iti.ecomus.dto.UpdateProfileDTO;
import org.iti.ecomus.dto.UserDTO;
import org.iti.ecomus.dto.UserSignUpDTO;
import org.iti.ecomus.entity.PasswordResetToken;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.exceptions.BadRequestException;
import org.iti.ecomus.exceptions.ConflictException;
import org.iti.ecomus.mappers.UserMapper;
import org.iti.ecomus.repository.PasswordResetTokenRepo;
import org.iti.ecomus.repository.UserRepo;
import org.iti.ecomus.util.MailMessenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserManager implements UserDetailsManager {

    @Autowired
    UserRepo userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordResetTokenRepo passwordResetTokenRepo;

    @Autowired
    private MailMessenger mailMessenger;

    @Override
    public void createUser(UserDetails userDetails) {

        User user = (User) userDetails;
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        User user = (User) userDetails;
        Optional<User> existingUserOpt = userRepository.findByEmail(user.getUsername());
        if (existingUserOpt.isEmpty()) {
            throw new UsernameNotFoundException("Cannot update non-existing user: " + user.getUsername());
        }
        userRepository.save(user);
    }

    public UserDTO updateUserProfile(User user, UpdateProfileDTO updateDTO) {

        // Check if email is being changed and if new email already exists
        if (!user.getEmail().equals(updateDTO.getEmail()) &&
                userRepository.existsByEmail(updateDTO.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        // Update user fields
        user.setEmail(updateDTO.getEmail());
        user.setPhone(updateDTO.getPhone());
        user.setCreditLimit(updateDTO.getCreditLimit());
        user.setCreditNo(updateDTO.getCreditNo());
        user.setJob(updateDTO.getJob());
        user.setUserName(updateDTO.getUserName());

        this.updateUser(user);

        UserDTO userDTO = userMapper.toUserDTO(userRepository.findById(user.getUserId()).get());

        return userDTO;
    }

    @Override
    public void deleteUser(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
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
            throw new BadRequestException("No authenticated user found to change password.");
        }

        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Old password does not match.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean userExists(String email) {

        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch the user from the repository by username
        Optional<User> userOptional = userRepository.findByEmail(email);

        // Check if the user exists
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(MessageFormat.format("User with username {0} not found", email));
        }

        // Return the UserDetails extracted from the User entity
        return userOptional.get();
    }

    public void createPasswordResetTokenForUser(User user, String token) {
        // Delete any existing tokens for this user
        passwordResetTokenRepo.deleteByUserUserId(user.getUserId());
        
        // Create new token
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setToken(token);
        myToken.setUser(user);
        myToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // Token valid for 24 hours
        
        passwordResetTokenRepo.save(myToken);
    }

    public String generatePasswordResetToken(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            // Don't reveal that the email doesn't exist for security reasons
            return null;
        }
        
        User user = userOpt.get();
        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(user, token);
        
        // Send email with reset link
        mailMessenger.sendresetPassword(user.getEmail(), token);
        
        return token;
    }

    public boolean validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> tokenOpt = passwordResetTokenRepo.findByToken(token);
        
        if (tokenOpt.isEmpty()) {
            return false;
        }
        
        PasswordResetToken resetToken = tokenOpt.get();
        
        if (resetToken.isExpired() || resetToken.isUsed()) {
            return false;
        }
        
        return true;
    }

    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = passwordResetTokenRepo.findByToken(token);
        
        if (tokenOpt.isEmpty() || tokenOpt.get().isExpired() || tokenOpt.get().isUsed()) {
            return false;
        }
        
        PasswordResetToken resetToken = tokenOpt.get();
        User user = resetToken.getUser();
        
        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        // Mark token as used
        resetToken.setUsed(true);
        passwordResetTokenRepo.save(resetToken);
        
        return true;
    }
}
