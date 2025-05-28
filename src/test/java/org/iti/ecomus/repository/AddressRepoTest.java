package org.iti.ecomus.repository;

import org.iti.ecomus.entity.Address;
import org.iti.ecomus.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AddressRepoTest {

    private AddressRepo addressRepo;

    @BeforeEach
    void setUp() {
        addressRepo = mock(AddressRepo.class);
    }

    @Test
    void testFindAddressByUserUserId() {
        // Arrange
        Long userId = 1L;

        User user = new User();
        user.setUserId(userId);
        user.setUserName("John Doe");

        Address address1 = new Address();
        address1.setId(101L);
        address1.setArea("Downtown");
        address1.setUser(user);

        Address address2 = new Address();
        address2.setId(102L);
        address2.setArea("Uptown");
        address2.setUser(user);

        List<Address> mockAddresses = List.of(address1, address2);

        when(addressRepo.findAddressByUserUserId(userId)).thenReturn(mockAddresses);

        // Act
        List<Address> result = addressRepo.findAddressByUserUserId(userId);

        // Assert
        assertEquals(2, result.size());
        result.forEach(addr -> {
            assertEquals(userId, addr.getUser().getUserId());
            assertNotNull(addr.getArea());
        });

        verify(addressRepo, times(1)).findAddressByUserUserId(userId);
    }
}
