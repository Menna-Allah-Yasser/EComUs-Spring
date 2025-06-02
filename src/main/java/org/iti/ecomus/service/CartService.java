package org.iti.ecomus.service;

import org.iti.ecomus.dto.CartDTO;
import org.iti.ecomus.dto.PagedResponse;
import org.iti.ecomus.paging.PagingAndSortingHelper;

import java.util.List;

public interface CartService {

    List<CartDTO> getCartItemsByUserId(Long userId);

    CartDTO getCartItem(Long userId, Long productId);

    void addOrUpdateCartItem(Long userId, Long productId, int quantity);

    void removeOrUpdateCartItem(Long userId, Long productId, int quantity);

    void removeCartItem(Long userId, Long productId);

    Integer getTotalQuantity(Long userId);

    Integer getTotalPrice(Long userId);
    Integer getProductTotalPrice(Long userId, Long productId);

    PagedResponse<CartDTO> getall(PagingAndSortingHelper helper, int pageNum, int pageSize, Long userId);
}
