package org.iti.ecomus.service;

import org.iti.ecomus.dto.WishlistDTO;
import java.util.List;

public interface WishlistService {
    List<WishlistDTO> getAllByUserId(Long userId);
    WishlistDTO getByUserIdProductId(Long userId, Long productId);
    WishlistDTO add(Long userId, Long productId);
    void delete(Long userId, Long productId);
}