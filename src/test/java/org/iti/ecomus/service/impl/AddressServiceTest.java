package org.iti.ecomus.service.impl;

import org.iti.ecomus.dto.AddressDTO;
import org.iti.ecomus.entity.*;
import org.iti.ecomus.exceptions.BadRequestException;
import org.iti.ecomus.exceptions.ResourceNotFoundException;
import org.iti.ecomus.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private WishlistRepo wishlistRepo;

    private User savedUser;

    private Address saved;

    private AddressDTO dto;

    @BeforeEach
    void setup() {
        wishlistRepo.deleteAll();  // If you have wishlistRepo
        addressRepo.deleteAll();   // Delete addresses that reference users
        userRepo.deleteAll();

        savedUser = new User();
        savedUser.setUserName("Test User");
        savedUser.setEmail("test@example.com");
        savedUser.setPhone("0123456789");
        savedUser.setPassword("password");
        savedUser = userRepo.save(savedUser);

        dto = new AddressDTO();
        dto.setCity("Cairo");
        dto.setArea("Nasr City");
        dto.setStreet("Street 10");
        dto.setBuildingNo("A1");
        dto.setUserId(savedUser.getUserId());

        saved = addressService.addAddress(dto);
    }

    @Nested
    class AddAddressTests {

        @Test
        void shouldAddAddressSuccessfully() {
            assertNotNull(saved.getId());
            assertEquals("Cairo", saved.getCity());
        }

        @Test
        void shouldThrowWhenDTOIsNull() {
            assertThrows(BadRequestException.class, () -> {
                addressService.addAddress(null);
            });
        }

        @Test
        void shouldThrowWhenUserIdIsInvalid() {
            dto.setUserId(-1L);
            assertThrows(BadRequestException.class, () -> {
                addressService.addAddress(dto);
            });
        }

        @Test
        void shouldThrowWhenUserNotFound() {
            dto.setUserId(99999L);

            assertThrows(ResourceNotFoundException.class, () -> {
                addressService.addAddress(dto);
            });
        }
    }

    @Nested
    class UpdateAddressTests {

        @Test
        void shouldUpdateAddressSuccessfully() {
            Address address = new Address();
            address.setCity("Old City");
            address.setArea("Old Area");
            address.setStreet("Old Street");
            address.setBuildingNo("Old Building");
            address.setUser(savedUser);
            Address saved = addressRepo.save(address);

            AddressDTO dto = new AddressDTO();
            dto.setId(saved.getId());
            dto.setCity("New City");
            dto.setArea("New Area");
            dto.setStreet("New Street");
            dto.setBuildingNo("New Building");
            dto.setUserId(savedUser.getUserId());

            addressService.updateAddress(dto);

            Address updated = addressRepo.findById(saved.getId()).orElseThrow();
            assertEquals("New City", updated.getCity());
            assertEquals("New Area", updated.getArea());
            assertEquals("New Street", updated.getStreet());
            assertEquals("New Building", updated.getBuildingNo());
            assertEquals(savedUser.getUserId(), updated.getUser().getUserId());
            assertEquals(saved.getId(), updated.getId());
        }

        @Test
        void shouldThrowWhenDTOIsNull() {
            assertThrows(BadRequestException.class,
                    () -> addressService.updateAddress(null));
        }

        @Test
        void shouldThrowWhenIdIsNull() {
            AddressDTO dto = new AddressDTO();
            dto.setCity("Test");

            assertThrows(BadRequestException.class,
                    () -> addressService.updateAddress(dto));
        }
    }

    @Nested
    class GetAddressesTests {

        @Test
        void shouldReturnAllAddressesForUser() {

            List<AddressDTO> result = addressService.getAddressesByUserId(savedUser.getUserId());

            assertEquals(1, result.size());
            assertEquals("Cairo", result.getFirst().getCity());
        }

        @Test
        void shouldReturnEmptyListForInvalidUser() {
            assertThrows(ResourceNotFoundException.class,
                    () -> addressService.getAddressesByUserId(9999L));
        }
    }
    
    @Nested
    class DeleteAddressTests {

        @Test
        void shouldDeleteAddressById() {
            Address address = new Address();
            address.setCity("ToDelete");
            address.setArea("Area");
            address.setStreet("Street");
            address.setBuildingNo("B1");
            address.setUser(savedUser);
            Address saved = addressRepo.save(address);

            addressService.deleteAddress(saved.getId());

            Optional<Address> result = addressRepo.findById(saved.getId());
            assertTrue(result.isEmpty());
        }
    }
}
