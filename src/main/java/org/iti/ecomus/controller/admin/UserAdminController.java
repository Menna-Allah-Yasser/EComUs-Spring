package org.iti.ecomus.controller.admin;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.iti.ecomus.config.AppConstants;
import org.iti.ecomus.dto.PagedResponse;
import org.iti.ecomus.dto.UserDTO;
import org.iti.ecomus.paging.PagingAndSortingHelper;
import org.iti.ecomus.paging.PagingAndSortingParam;
import org.iti.ecomus.service.UserService;
import org.iti.ecomus.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Admin - Users", description = "Admin user management")
@Validated
public class UserAdminController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<UserDTO>> getAllUsers(@PagingAndSortingParam(
                                                                 model = AppConstants.USER_MODEL,
                                                                 defaultSortField = "userId"
                                                         ) PagingAndSortingHelper helper,
                                                              @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int pageNum,
                                                              @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize) {
        return ResponseEntity.ok(userService.getAllUsers(helper, pageNum, pageSize));
    }

    @GetMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") @Min(1) Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }




}
