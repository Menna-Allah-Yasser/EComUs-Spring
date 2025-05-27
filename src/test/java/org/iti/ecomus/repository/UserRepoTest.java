package org.iti.ecomus.repository;


import org.iti.ecomus.annotation.IntegrationTest;
import org.iti.ecomus.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class UserRepoTest {

    @Autowired
    private UserRepo userRepository;

    @Test
    void testFind() {
        // Given
        User user = userRepository.findById(1L).get();
        user.getCarts().forEach(cart -> {
            System.out.println("Cart quantity " + cart.getQuantity());
            System.out.println("Cart product " + cart.getProduct().getProductName());

        });

    }
}
