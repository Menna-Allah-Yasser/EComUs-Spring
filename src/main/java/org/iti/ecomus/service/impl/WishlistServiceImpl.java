package org.iti.ecomus.service.impl;

import lombok.RequiredArgsConstructor;
import org.iti.ecomus.dto.WishlistDTO;
import org.iti.ecomus.entity.*;
import org.iti.ecomus.exceptions.ResourceNotFoundException;
import org.iti.ecomus.mappers.WishlistMapper;
import org.iti.ecomus.repository.ProductRepo;
import org.iti.ecomus.repository.UserRepo;
import org.iti.ecomus.repository.WishlistRepo;
import org.iti.ecomus.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepo wishlistRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private WishlistMapper wishlistMapper;

    @Override
    public List<WishlistDTO> getAllByUserId(Long userId) {
        return wishlistRepo.findByUserUserId(userId).stream()
                .map( wishlistMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public WishlistDTO getByUserIdProductId(Long userId, Long productId) {
        WishlistPK pk = new WishlistPK(userId, productId);
        Wishlist wishlist = wishlistRepo.findById(pk)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist item not found"));
        return wishlistMapper.toDTO(wishlist);
    }

    @Override
    public WishlistDTO add(Long userId, Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Wishlist wishlist = new Wishlist(userId, productId, user, product);

        Wishlist saved = wishlistRepo.save(wishlist);
        return wishlistMapper.toDTO(saved) ;
    }

    @Override
    public void delete(Long userId, Long productId) {
        wishlistRepo.deleteById(new WishlistPK(userId, productId));
    }
}