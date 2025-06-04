package org.iti.ecomus.service.impl;

import org.iti.ecomus.dto.AddressDTO;
import org.iti.ecomus.entity.*;
import org.iti.ecomus.exceptions.*;
import org.iti.ecomus.mappers.AddressMapper;
import org.iti.ecomus.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AddressService {

    private final AddressRepo addressRepo;
    private final UserRepo userRepo;
    private final AddressMapper addressMapper;


    @Autowired
    public AddressService(AddressRepo addressRepo, AddressMapper addressMapper, UserRepo userRepo) {
        this.addressRepo = addressRepo;
        this.userRepo = userRepo;
        this.addressMapper = addressMapper;
    }

    public List<AddressDTO> getAllAddresses() {
        List<Address> addresses = addressRepo.findAll();

        if (addresses.isEmpty()) {
            throw new ResourceNotFoundException("No addresses found.");
        }

        return addressMapper.toDtoList(addresses);
    }

    public AddressDTO getAddressById(Long addressId) {
        if (addressId == null || addressId <= 0) {
            throw new BadRequestException("Invalid address ID: " + addressId);
        }

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + addressId));

        return addressMapper.toDto(address);
    }

    public List<AddressDTO> getAddressesByUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new BadRequestException("Invalid user ID: " + userId);
        }

        if (!userRepo.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        List<Address> addresses = addressRepo.findAddressByUserUserId(userId);
        return addressMapper.toDtoList(addresses);
    }

    public AddressDTO addAddress(Long userID, AddressDTO addressDTO) {
        if (userID == null || userID <= 0) {
            throw new BadRequestException("Invalid or missing user ID.");
        }

        if (addressDTO == null) {
            throw new BadRequestException("Address data is missing.");
        }

        // Validate user existence
        User user = userRepo.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userID));

        // Convert DTO to Entity
        Address address = addressMapper.toEntity(addressDTO);

        // Ensure the address ID is null (to avoid unintended updates)
        address.setId(null);
        address.setUser(user);

        // Save and return the result
        Address saved = addressRepo.save(address);
        return addressMapper.toDto(saved);
    }

    public AddressDTO updateAddress(Long userID, AddressDTO addressDTO) {
        if (addressDTO == null || addressDTO.getId() == null) {
            throw new BadRequestException("Address data is incomplete or null.");
        }

        // Validate user existence
        User user = userRepo.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userID));

        // Validate address existence and ownership
        Address existingAddress = addressRepo.findById(addressDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + addressDTO.getId()));

        if (!existingAddress.getUser().getUserId().equals(userID)) {
            throw new BadRequestException("Address does not belong to the specified user.");
        }

        // Update fields (only the ones that are allowed to be updated)
        existingAddress.setCity(addressDTO.getCity());
        existingAddress.setArea(addressDTO.getArea());
        existingAddress.setStreet(addressDTO.getStreet());
        existingAddress.setBuildingNo(addressDTO.getBuildingNo());

        // Save updated address
        Address saved = addressRepo.save(existingAddress);
        return addressMapper.toDto(saved);
    }

    public void deleteAddress(Long userId, Long addressId) {
        if (userId == null || userId <= 0 || addressId == null || addressId <= 0) {
            throw new BadRequestException("Invalid input.");
        }

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found."));

        if (!address.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("You are not authorized to delete this address.");
        }

        addressRepo.delete(address);
    }
}
