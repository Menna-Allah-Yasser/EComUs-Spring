package org.iti.ecomus.controller.admin;

import org.iti.ecomus.config.AppConstants;
import org.iti.ecomus.dto.PagedResponse;
import org.iti.ecomus.dto.UserDTO;
import org.iti.ecomus.paging.PagingAndSortingHelper;
import org.iti.ecomus.paging.PagingAndSortingParam;
import org.iti.ecomus.service.UserService;
import org.iti.ecomus.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class UserAdminController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public ResponseEntity<PagedResponse<UserDTO>> getAllUsers(@PagingAndSortingParam(
                                                                 model = AppConstants.USER_MODEL,
                                                                 defaultSortField = "userId"
                                                         ) PagingAndSortingHelper helper,
                                                              @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int pageNum,
                                                              @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize) {
        return ResponseEntity.ok(userService.getAllUsers(helper, pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }




}
