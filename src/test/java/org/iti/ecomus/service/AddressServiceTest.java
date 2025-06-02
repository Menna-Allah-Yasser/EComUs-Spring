package org.iti.ecomus.service;

import org.iti.ecomus.dto.AddressDTO;
import org.iti.ecomus.entity.*;
import org.iti.ecomus.exceptions.BadRequestException;
import org.iti.ecomus.exceptions.ResourceNotFoundException;
import org.iti.ecomus.repository.*;
import org.iti.ecomus.service.impl.AddressService;
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

    private AddressDTO saved;

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


        saved = addressService.addAddress(savedUser.getUserId(),dto);
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
                addressService.addAddress(savedUser.getUserId(),null);
            });
        }

        @Test
        void shouldThrowWhenUserIdIsInvalid() {
            assertThrows(BadRequestException.class, () -> {
                addressService.addAddress(-1L,dto);
            });
        }

        @Test
        void shouldThrowWhenUserNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> {
                addressService.addAddress(99999L,dto);
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

            addressService.updateAddress(savedUser.getUserId(),dto);

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
                    () -> addressService.updateAddress(savedUser.getUserId(),null));
        }

        @Test
        void shouldThrowWhenIdIsNull() {
            AddressDTO dto = new AddressDTO();
            dto.setCity("Test");

            assertThrows(BadRequestException.class,
                    () -> addressService.updateAddress(null,dto));
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

            addressService.deleteAddress(savedUser.getUserId() ,saved.getId());

            Optional<Address> result = addressRepo.findById(saved.getId());
            assertTrue(result.isEmpty());
        }
    }
}
