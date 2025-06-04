package org.iti.ecomus.controller;

import jakarta.annotation.security.RolesAllowed;
import org.iti.ecomus.config.AppConstants;
import org.iti.ecomus.dto.PagedResponse;
import org.iti.ecomus.mappers.UserMapper;
import org.iti.ecomus.paging.PagingAndSortingHelper;
import org.iti.ecomus.paging.PagingAndSortingParam;
import org.iti.ecomus.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.iti.ecomus.dto.UserDTO;
import org.iti.ecomus.entity.User;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController2 {
    @Autowired
    UserRepo userRepository;

    @Autowired
    UserMapper userMapper;

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#user.userId == #id")
    public ResponseEntity user(@AuthenticationPrincipal User user, @PathVariable("id")   Long id) {
//        System.out.println("User ID: " + user.getUserId());
        UserDTO userDTO = userMapper.toUserDTO(userRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("User not found")));

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<UserDTO>> users(@AuthenticationPrincipal User user, @PagingAndSortingParam(
                                           model = AppConstants.USER_MODEL,
                                           defaultSortField = "userId"
                                   ) PagingAndSortingHelper helper,
                                     @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int pageNum,
                                     @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize) {
        PagedResponse<User> pagedResponse = helper.getPagedResponse(pageNum, pageSize, userRepository,null);
        PagedResponse<UserDTO> resp = pagedResponse.mapContent(userMapper::toUserDTOs);
        return ResponseEntity.ok( resp);
    }

    @PutMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed("ADMIN")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<UserDTO>> allUsers(@AuthenticationPrincipal User user) {
//        System.out.println("User ID: " + user.getUserId());
//        System.out.println("User authorities: " + user.getAuthorities());
        List<UserDTO> userDTOs = userMapper.toUserDTOs(userRepository.findAll());
        return ResponseEntity.ok(userDTOs);
    }



}
