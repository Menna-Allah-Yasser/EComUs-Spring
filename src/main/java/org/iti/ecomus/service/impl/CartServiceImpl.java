package org.iti.ecomus.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.iti.ecomus.dto.CartDTO;
import org.iti.ecomus.entity.Cart;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.mappers.CartMapper;
import org.iti.ecomus.repository.CartRepo;
import org.iti.ecomus.repository.ProductRepo;
import org.iti.ecomus.repository.UserRepo;
import org.iti.ecomus.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final CartMapper cartMapper;

    public CartServiceImpl(CartRepo cartRepo, UserRepo userRepo, ProductRepo productRepo, CartMapper cartMapper) {
        this.cartRepo = cartRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.cartMapper = cartMapper;
    }

    @Override
    public List<CartDTO> getCartItemsByUserId(Long userId) {
        List<Cart> carts = cartRepo.findByUserUserId(userId);
        return carts.stream()
                .map(cartMapper::toCartDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CartDTO getCartItem(Long userId, Long productId) {
        Cart cart = cartRepo.findByUserUserIdAndProductProductId(userId, productId);
        if (cart == null) return null;
        return cartMapper.toCartDTO(cart);
    }
@Override
public void addOrUpdateCartItem(Long userId, Long productId, int quantity) {
    User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

    Cart cart = cartRepo.findByUserUserIdAndProductProductId(userId, productId);
    if (cart == null) {
        cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(quantity);
        cart.setUserId(user.getUserId());            
        cart.setProductId(product.getProductId());   
    } else {
        cart.setQuantity(cart.getQuantity() + quantity);
    }
    cartRepo.save(cart);
}


    @Override
    public void removeCartItem(Long userId, Long productId) {
        cartRepo.deleteByUserUserIdAndProductProductId(userId, productId);
    }

    @Override
    public Integer getTotalQuantity(Long userId) {
        return cartRepo.getTotalQuantityByUserId(userId);
    }

    @Override
    public Integer getTotalPrice(Long userId) {
        return cartRepo.calculateCartTotal(userId);
    }
}
