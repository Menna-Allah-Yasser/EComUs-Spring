package org.iti.ecomus.service.impl;

import java.util.List;
import java.util.Optional;

import org.iti.ecomus.dto.CartDTO;
import org.iti.ecomus.entity.Cart;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.mappers.CartMapper;
import org.iti.ecomus.repository.CartRepo;
import org.iti.ecomus.repository.ProductRepo;
import org.iti.ecomus.repository.UserRepo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class CartServiceImplTest {

    @Mock
    private CartRepo cartRepo;

    @Mock
    private CartMapper cartMapper;
      @Mock
    private UserRepo userRepo;
  @Mock
private ProductRepo productRepo;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCartItemsByUserId() {
        Long userId = 1L;
        Cart cart = new Cart();
        CartDTO cartDTO = new CartDTO();
        List<Cart> carts = List.of(cart);

        when(cartRepo.findByUserUserId(userId)).thenReturn(carts);
        when(cartMapper.toCartDTO(cart)).thenReturn(cartDTO);

        List<CartDTO> result = cartService.getCartItemsByUserId(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(cartRepo, times(1)).findByUserUserId(userId);
        verify(cartMapper, times(1)).toCartDTO(cart);
    }

 @Test
void testAddOrUpdateCartItem_NewItem() {
    Long userId = 1L;
    Long productId = 19L;
    int quantity = 2;

    User mockUser = new User();
    mockUser.setUserId(userId);
    mockUser.setUserName("TestUser"); 

    Product mockProduct = new Product();
    mockProduct.setProductId(productId);
   

    when(userRepo.findById(userId)).thenReturn(Optional.of(mockUser));
    when(productRepo.findById(productId)).thenReturn(Optional.of(mockProduct));

    when(cartRepo.findByUserUserIdAndProductProductId(userId, productId)).thenReturn(null);
    when(cartRepo.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

    cartService.addOrUpdateCartItem(userId, productId, quantity);

    verify(cartRepo).save(any(Cart.class));
}



    @Test
    void testRemoveCartItem() {
        Long userId = 1L;
        Long productId = 19L;

        doNothing().when(cartRepo).deleteByUserUserIdAndProductProductId(userId, productId);

        cartService.removeCartItem(userId, productId);

        verify(cartRepo, times(1)).deleteByUserUserIdAndProductProductId(userId, productId);
    }

    @Test
    void testGetTotalQuantity() {
        Long userId = 1L;
        when(cartRepo.getTotalQuantityByUserId(userId)).thenReturn(10);

        Integer totalQuantity = cartService.getTotalQuantity(userId);

        assertEquals(10, totalQuantity);
    }

    @Test
    void testGetTotalPrice() {
        Long userId = 1L;
        when(cartRepo.calculateCartTotal(userId)).thenReturn(500);

        Integer totalPrice = cartService.getTotalPrice(userId);

        assertEquals(500, totalPrice);
    }
}
