package org.iti.ecomus.controller.customer;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.iti.ecomus.dto.ChangePasswordDTO;
import org.iti.ecomus.dto.UpdateProfileDTO;
import org.iti.ecomus.dto.UserDTO;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/users")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Customer - Profile", description = "Customer profile management")
@Validated
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping(path = "/profile",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> profile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok( userService.getUserById(user.getUserId()));
    }

    @PutMapping(path = "/profile",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateProfile(@AuthenticationPrincipal User user,@Valid @RequestBody UpdateProfileDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(user, userDTO));
    }

    @PutMapping(path = "/profile/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal User user,@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.updatePassword(changePasswordDTO);
        return ResponseEntity.noContent().build();
    }

}
