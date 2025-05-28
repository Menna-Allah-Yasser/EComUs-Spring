package org.iti.ecomus.repository;

import java.util.List;

import org.iti.ecomus.annotation.IntegrationTest;
import org.iti.ecomus.entity.Cart;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class CartRepoTest {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

   
    @Test
    void testFindByUserUserId() {
        Long userId = 1L; 
        
        List<Cart> carts = cartRepo.findByUserUserId(userId);
        
        System.out.println("=== Test findByUserUserId ===");
        System.out.println("User ID: " + userId);
        System.out.println("Cart items found: " + carts.size());
        
        for (Cart cart : carts) {
            System.out.println("- Product: " + cart.getProduct().getProductName() + 
                             ", Quantity: " + cart.getQuantity());
        }
        
        assertNotNull(carts);
    }


    @Test
    void testFindByUserUserIdAndProductProductId() {
        Long userId = 1L;
        Long productId = 19L;
        
        Cart cart = cartRepo.findByUserUserIdAndProductProductId(userId, productId);
        
        System.out.println("=== Test findByUserUserIdAndProductProductId ===");
        System.out.println("Searching for User ID: " + userId + ", Product ID: " + productId);
        
        if (cart != null) {
            System.out.println("Cart item found!");
            System.out.println("- Product: " + cart.getProduct().getProductName());
            System.out.println("- Quantity: " + cart.getQuantity());
            System.out.println("- Price: " + cart.getProduct().getPrice());
        } else {
            System.out.println("No cart item found for this user and product combination");
        }
    }

    // Test Method 3: getTotalQuantityByUserId
    @Test
    void testGetTotalQuantityByUserId() {
        Long userId = 1L; 
        
        Integer totalQuantity = cartRepo.getTotalQuantityByUserId(userId);
        
        System.out.println("=== Test getTotalQuantityByUserId ===");
        System.out.println("User ID: " + userId);
        System.out.println("Total quantity in cart: " + totalQuantity);
        
        if (totalQuantity != null) {
            assertTrue(totalQuantity >= 0);
        }
    }

    // Test Method 4: calculateCartTotal
    @Test
    void testCalculateCartTotal() {
        Long userId = 1L;
        
        Integer totalPrice = cartRepo.calculateCartTotal(userId);
        
        System.out.println("=== Test calculateCartTotal ===");
        System.out.println("User ID: " + userId);
        System.out.println("Total cart price: " + totalPrice);
        
        if (totalPrice != null) {
            assertTrue(totalPrice >= 0);
        }
    }



// Test Method 5: deleteByUserUserIdAndProductProductId
 @Test

 void testDeleteByUserUserIdAndProductProductId() {
     Long userId = 1L;
     Long productId = 19L;
    
     System.out.println("=== Test deleteByUserUserIdAndProductProductId ===");
    
     Cart existingCart = cartRepo.findByUserUserIdAndProductProductId(userId, productId);
    
     if (existingCart != null) {
         System.out.println("Cart item found before deletion:");
         System.out.println("- Product: " + existingCart.getProduct().getProductName() +
                          ", Quantity: " + existingCart.getQuantity());
        
    
         cartRepo.deleteByUserUserIdAndProductProductId(userId, productId);
        
         System.out.println("Delete method executed");

         Cart deletedCart = cartRepo.findByUserUserIdAndProductProductId(userId, productId);
         assertNull(deletedCart, "Cart item should be deleted");
         System.out.println(" Deletion successful - item no longer exists in cart");
        
     } else {
         System.out.println("No cart item found for User ID: " + userId + " and Product ID: " + productId);
         System.out.println("Please make sure this combination exists in your database before running the test");
         fail("Test requires existing cart item to verify deletion");
     }
 }

}
