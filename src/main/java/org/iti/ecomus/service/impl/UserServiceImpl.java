package org.iti.ecomus.service.impl;

import org.iti.ecomus.config.security.UserManager;
import org.iti.ecomus.dto.*;
import org.iti.ecomus.entity.Order;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.exceptions.ConflictException;
import org.iti.ecomus.mappers.UserMapper;
import org.iti.ecomus.paging.PagingAndSortingHelper;
import org.iti.ecomus.repository.UserRepo;
import org.iti.ecomus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserManager userManager;

    @Override
    public List<UserDTO> getAllUsers() {
        return userMapper.toUserDTOs( userRepo.findAll());
    }

    @Transactional(readOnly = true)
    public PagedResponse<UserDTO> getAllUsers(PagingAndSortingHelper helper, int pageNum, int pageSize) {
        PagedResponse<User> pagedResponse = helper.getPagedResponse(pageNum, pageSize, userRepo,null);
        PagedResponse<UserDTO> resp = pagedResponse.mapContent(userMapper::toUserDTOs);
        return resp;
    }

    @Override
    public UserDTO getUserById(Long userId) {
        return userMapper.toUserDTO( userRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId)));
    }

    public void updatePassword(ChangePasswordDTO changePasswordDTO){
        userManager.changePassword(changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
    }

    public UserDTO updateUser(User user,UpdateProfileDTO profileDTO){
        return userManager.updateUserProfile(user, profileDTO);
    }

    public String requestPasswordReset(String email) {
        return userManager.generatePasswordResetToken(email);
    }

    public boolean validateResetToken(String token) {
        return userManager.validatePasswordResetToken(token);
    }

    public boolean resetPassword(PasswordResetDTO resetDTO) {
        return userManager.resetPassword(resetDTO.getToken(), resetDTO.getNewPassword());
    }

}
