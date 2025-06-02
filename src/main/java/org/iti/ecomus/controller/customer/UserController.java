package org.iti.ecomus.controller.customer;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.iti.ecomus.dto.ChangePasswordDTO;
import org.iti.ecomus.dto.UpdateProfileDTO;
import org.iti.ecomus.dto.UserDTO;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/users")
@SecurityRequirement(name = "E-Commerce Application")
@Tag(name = "Customer - Profile", description = "Customer profile management")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> profile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok( userService.getUserById(user.getUserId()));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateProfile(@AuthenticationPrincipal User user, UpdateProfileDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(user, userDTO));
    }

    @PutMapping("/profile/password")
    public ResponseEntity<String> updatePassword(@AuthenticationPrincipal User user, ChangePasswordDTO changePasswordDTO) {
        userService.updatePassword(changePasswordDTO);
        return ResponseEntity.ok("Password updated successfully");
    }

}
