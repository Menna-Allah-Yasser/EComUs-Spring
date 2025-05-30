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
        List<Address> addressList = addressRepo.findAll();
        return addressMapper.toDtoList(addressList);
    }

    public AddressDTO getAddressById(Long addressId) {
        if (addressId == null || addressId < 0) {
            throw new BadRequestException("Invalid address id: " + addressId);
        }

        Address address = addressRepo.findById(addressId).orElse(null);

        if (address == null) {
            throw new ResourceNotFoundException("Address not found with id: " + addressId);
        }

        return addressMapper.toDto(address);
    }

    public List<AddressDTO> getAddressesByUserId(Long userId) {
        if (userId == null || userId < 0) {
            throw new BadRequestException("Invalid user id: " + userId);
        }
        if (!userRepo.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        List<Address> addressList = addressRepo.findAddressByUserUserId(userId);
        return addressMapper.toDtoList(addressList);
    }

    public Address addAddress(AddressDTO addressDTO) {
        if (addressDTO == null || addressDTO.getUserId() == null || addressDTO.getUserId() < 0) {
            throw new BadRequestException("Invalid address or missing user");
        }

        // Validate user existence
        Optional<User> optionalUser = userRepo.findById(addressDTO.getUserId());
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

        // Convert DTO to Entity
        Address address = addressMapper.toEntity(addressDTO);
        address.setUser(optionalUser.get()); // Link to existing user

        return addressRepo.save(address);
    }

    public void updateAddress(AddressDTO addressDTO) {
        if (addressDTO == null || addressDTO.getId() == null) {
            throw new BadRequestException("Invalid address");
        }

        // Validate user existence
        Optional<User> optionalUser = userRepo.findById(addressDTO.getUserId());
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

        Address address = addressMapper.toEntity(addressDTO);
        address.setUser(optionalUser.get());

        addressRepo.save(address);
    }

    public void deleteAddress(Long addressId) {
        if (addressId == null || addressId < 0) {
            throw new BadRequestException("Invalid address id: " + addressId);
        }
        Optional<Address> optionalAddress = addressRepo.findById(addressId);
        if (optionalAddress.isEmpty()) {
            throw new ResourceNotFoundException("Address not found with id: " + addressId);
        }
        addressRepo.deleteById(addressId);
    }
}
