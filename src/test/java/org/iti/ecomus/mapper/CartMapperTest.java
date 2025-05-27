package org.iti.ecomus.mapper;

import org.iti.ecomus.annotation.IntegrationTest;
import org.iti.ecomus.dto.CartDTO;
import org.iti.ecomus.dto.CategoryDTO;
import org.iti.ecomus.dto.CategoryNoProductDTO;
import org.iti.ecomus.dto.ProductDTO;
import org.iti.ecomus.entity.Cart;
import org.iti.ecomus.entity.Category;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.mappers.CartMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class CartMapperTest {

    @Autowired
    private CartMapper cartMapper;

    @Test
    void testToCartDTO() {
        // Given
        // Create a sample Cart entity object
        Cart cart = new Cart();
        Product product = new Product("Sample Product", "Sample Description", 2, new BigDecimal("99.99"));
        product.setCategories(Arrays.asList(new Category("Sample Category")));
        cart.setProduct(product);
        cart.setQuantity(20);
        // When
        // Convert entity to DTO using CartMapper
        CartDTO cartDTO = cartMapper.toCartDTO(cart);

        // Then
        assertNotNull(cartDTO);
        assertEquals(cartDTO.getProduct().getProductId(), cart.getProduct().getProductId());
        assertEquals(cartDTO.getProduct().getProductName(), cart.getProduct().getProductName());
        assertEquals(cartDTO.getProduct().getDescription(), cart.getProduct().getDescription());
        assertEquals(cartDTO.getProduct().getQuantity(), cart.getProduct().getQuantity());
        assertEquals(cartDTO.getProduct().getPrice(), cart.getProduct().getPrice());
        assertEquals(cartDTO.getProduct().getPrice(), cart.getProduct().getPrice());
        assertEquals(cartDTO.getProduct().getCategories().size(), cart.getProduct().getCategories().size());
        assertEquals(cartDTO.getProduct().getCategories().get(0).getCategoryName(), "Sample Category");
        assertEquals(cartDTO.getQuantity(), cart.getQuantity());
    }

    @Test
    void testToCartEntity() {
        // Given
        // Create a sample CartDTO object
        CartDTO cartDTO = new CartDTO();
        cartDTO.setProduct(new ProductDTO(1L,"Sample Product", "Sample Description" , 2, new BigDecimal("99.99"),
                Arrays.asList(new CategoryNoProductDTO(1L, "Sample Category"))));
        cartDTO.setQuantity(20);

        // When
        // Convert DTO to entity using CartMapper
        Cart cart = cartMapper.toCart(cartDTO);

        // Then
        assertNotNull(cart);
        assertEquals(cart.getProduct().getProductId(), cartDTO.getProduct().getProductId());
        assertEquals(cart.getProduct().getProductName(), cartDTO.getProduct().getProductName());
        assertEquals(cart.getProduct().getDescription(), cartDTO.getProduct().getDescription());
        assertEquals(cart.getProduct().getQuantity(), cartDTO.getProduct().getQuantity());
        assertEquals(cart.getProduct().getPrice(), cartDTO.getProduct().getPrice());
        assertEquals(cart.getProduct().getCategories(), cart.getProduct().getCategories());
        assertEquals(cart.getQuantity(), cartDTO.getQuantity());
    }
}
