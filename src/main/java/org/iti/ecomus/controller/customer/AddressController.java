package org.iti.ecomus.controller.customer;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.iti.ecomus.dto.AddressDTO;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.service.impl.AddressService;
import org.iti.ecomus.util.validation.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/address")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Customer - Addresses", description = "Customer address management")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddresses(@AuthenticationPrincipal User user) {
        List<AddressDTO> addresses = addressService.getAddressesByUserId(user.getUserId());
        return ResponseEntity.ok(addresses);
    }

    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@AuthenticationPrincipal User user,@Validated @RequestBody AddressDTO addressDTO) {
        AddressDTO createdAddress = addressService.addAddress(user.getUserId(), addressDTO);
        return ResponseEntity.ok(createdAddress);
    }

    @PutMapping
    public ResponseEntity<AddressDTO> updateAddress(@AuthenticationPrincipal User user,@Validated(OnUpdate.class) @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddress = addressService.updateAddress(user.getUserId(), addressDTO);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@AuthenticationPrincipal User user, @PathVariable("id") Long addressId) {
        addressService.deleteAddress(user.getUserId(), addressId);
        return ResponseEntity.noContent().build();
    }

}
