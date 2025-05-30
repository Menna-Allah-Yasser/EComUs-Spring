package org.iti.ecomus.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.iti.ecomus.dto.CartDTO;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.repository.CartRepo;
import org.iti.ecomus.repository.ProductRepo;
import org.iti.ecomus.repository.UserRepo;
import org.iti.ecomus.service.CartService;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional

public class CartServiceIntegrationTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartRepo cartRepo;

    private Long userId;
    private Long productId;

    @BeforeEach
    void setUp() {
   
        User user = new User();
        user.setUserName("IntegrationUser");
        user.setEmail("test@example.com");
          user.setPassword("12345");
        user = userRepo.save(user);
        userId = user.getUserId();

    Product product = new Product();
product.setProductName("IntegrationProduct");
product.setDescription("Test product description");
product.setQuantity(10);
product.setPrice(BigDecimal.valueOf(100));
product = productRepo.save(product);
productId = product.getProductId();
    }

  @Test
void testAddOrUpdateCartItem_Integration() {
  // First : add product to cart 
    cartService.addOrUpdateCartItem(userId, productId, 3);

    var cart = cartRepo.findByUserUserIdAndProductProductId(userId, productId);
    assertNotNull(cart);
    assertEquals(3, cart.getQuantity());

   // second update quantity
    cartService.addOrUpdateCartItem(userId, productId, 2);

    var updatedCart = cartRepo.findByUserUserIdAndProductProductId(userId, productId);
    assertNotNull(updatedCart);
    assertEquals(5, updatedCart.getQuantity());
}

    @Test
    void testGetCartItemsByUserId_Integration() {
        cartService.addOrUpdateCartItem(userId, productId, 3);
        List<CartDTO> cartItems = cartService.getCartItemsByUserId(userId);

        assertNotNull(cartItems);
        assertFalse(cartItems.isEmpty());
        assertEquals(1, cartItems.size());
        assertEquals(productId, cartItems.get(0).getProduct().getProductId());
        assertEquals(3, cartItems.get(0).getQuantity());
    }


     @Test
    void testGetCartItem_Integration() {
        cartService.addOrUpdateCartItem(userId, productId, 4);
        CartDTO cartItem = cartService.getCartItem(userId, productId);

        assertNotNull(cartItem);
        assertEquals(productId, cartItem.getProduct().getProductId());
        assertEquals(4, cartItem.getQuantity());
    }

    @Test
    void testRemoveCartItem_Integration() {
        cartService.addOrUpdateCartItem(userId, productId, 2);
        cartService.removeCartItem(userId, productId);
        var cart = cartRepo.findByUserUserIdAndProductProductId(userId, productId);
        assertNull(cart);
    }

    @Test
    void testGetTotalQuantity_Integration() {
        cartService.addOrUpdateCartItem(userId, productId, 2);
        Integer quantity = cartService.getTotalQuantity(userId);

        assertNotNull(quantity);
        assertEquals(2, quantity);
    }

    @Test
    void testGetTotalPrice_Integration() {
        cartService.addOrUpdateCartItem(userId, productId, 2);
        Integer total = cartService.getTotalPrice(userId);

        assertNotNull(total);
        assertEquals(200, total); // 2 * 100
    }

    @Test
    void testRemoveOrUpdateCartItem_Integration() {
        cartService.addOrUpdateCartItem(userId, productId, 5);
        cartService.removeOrUpdateCartItem(userId, productId, 2);

        var cart = cartRepo.findByUserUserIdAndProductProductId(userId, productId);
        assertNotNull(cart);
        assertEquals(3, cart.getQuantity());

        // Test removing all items
        cartService.removeOrUpdateCartItem(userId, productId, 3);
        cart = cartRepo.findByUserUserIdAndProductProductId(userId, productId);
        assertNull(cart);
    }


}
