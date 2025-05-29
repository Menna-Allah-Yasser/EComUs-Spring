package org.iti.ecomus.repository;

import org.iti.ecomus.entity.Address;
import org.iti.ecomus.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AddressRepoTest {

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private UserRepo userRepo;

    private User testUser;

    @BeforeEach
    void setup() {
        // Save test user with all required fields
        testUser = new User();
        testUser.setUserName("John Doe");
        testUser.setEmail("john@example.com");
        testUser.setPassword("123456");

        userRepo.save(testUser);

        // Save addresses
        Address a1 = new Address();
        a1.setArea("Downtown");
        a1.setUser(testUser);

        Address a2 = new Address();
        a2.setArea("Suburb");
        a2.setUser(testUser);

        addressRepo.save(a1);
        addressRepo.save(a2);
    }

    @Test
    void testFindAddressByUserUserId_ReturnsCorrectAddresses() {
        List<Address> result = addressRepo.findAddressByUserUserId(testUser.getUserId());
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(a -> a.getArea().equals("Downtown")));
        assertTrue(result.stream().anyMatch(a -> a.getArea().equals("Suburb")));
    }
}
